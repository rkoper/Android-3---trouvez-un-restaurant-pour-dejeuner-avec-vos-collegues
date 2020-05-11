package com.m.sofiane.go4lunch.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.m.sofiane.go4lunch.BuildConfig;
import com.m.sofiane.go4lunch.R;
import com.m.sofiane.go4lunch.activity.subactivity;
import com.m.sofiane.go4lunch.models.pojoAutoComplete.AutoComplete;
import com.m.sofiane.go4lunch.models.pojoAutoComplete.Prediction;
import com.m.sofiane.go4lunch.services.Singleton;
import com.m.sofiane.go4lunch.services.googleInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapFragment extends Fragment implements  OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener  {

    private SupportMapFragment mMFragment;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    Marker mRestoMarker;
    LatLng mLatLng;
    LatLng mLatLngForAll;
    String placeId;
    String placeIdToCompare;
    @BindView(R.id.menu_search)
    MenuItem mSearch;
    View view;
    String apiKey = BuildConfig.APIKEY;

    SearchMapAdapter mAdapter;
    ArrayList<Prediction> listdataForSearch ;
    RecyclerView mRecyclerView;
    FragmentManager mFragmentManager;
    Map<String, LatLng> mMapToCompare;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, null);
        uploadToolbar();
        this.configureRecyclerView(view);
        ArrayList<Prediction> mT =new ArrayList<>();
        setHasOptionsMenu(true);
        loadMap();
        setRetainInstance(true);

        return view;
    }

    private void configureRecyclerView(View view) {
        this.listdataForSearch = new ArrayList<>();
        this.mAdapter = new SearchMapAdapter(listdataForSearch, mFragmentManager, getContext());
        mRecyclerView = view.findViewById(R.id.recyclerview_for_maps);
        mRecyclerView.setVisibility(view.INVISIBLE);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
    }

    private void uploadToolbar() {
        BottomNavigationView mBottomNavigationView = getActivity().findViewById(R.id.activity_main_bottom_navigation);
        mBottomNavigationView.setVisibility(mBottomNavigationView.VISIBLE);
        TextView mTitleText = (TextView) getActivity().findViewById(R.id.toolbar_title);
        mTitleText.setText(" I'm Hungry!");
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        Toolbar mToolbar = (Toolbar) getActivity().findViewById(R.id.activity_main_toolbar);

        inflater.inflate(R.menu.activity_main_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        setSearchTextColour(searchView);

        searchView.setOnSearchClickListener(v -> DoAfterClickOnSearch(mToolbar, searchView));
        searchView.setOnCloseListener(() -> {
            mToolbar.setNavigationIcon(R.drawable.ic_dehaze_black_24dp);
            initRestaurantPosition();
            return false;
        });
    }

        public void DoAfterClickOnSearch(Toolbar mToolbar, SearchView searchView){
            mToolbar.setNavigationIcon(null);
            initQuery(searchView);

        }

    public void initQuery(SearchView searchView){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String item) {
                Toast.makeText(getContext(), "QUERY", Toast.LENGTH_SHORT).show();
                if (item.length() != 0) {
                    System.out.println("1-------->");
                    build_retrofit_and_get_responseForSearch(view, item);}



                else{
                    mRecyclerView.setVisibility(View.GONE);
                    Toolbar mToolbar = (Toolbar) getActivity().findViewById(R.id.activity_main_toolbar);
                    mToolbar.setNavigationIcon(R.drawable.ic_dehaze_black_24dp);
                    initRestaurantPosition();
                }
                return false;
            }
        });
    }

    private void setSearchTextColour(SearchView searchView) {
        searchView.setMaxWidth(Integer.MAX_VALUE);
        int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchPlate = (EditText) searchView.findViewById(searchPlateId);
        searchPlate.setTextColor(getResources().getColor(R.color.Red));
        searchPlate.setBackgroundResource(R.color.tw__solid_white);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void build_retrofit_and_get_responseForSearch (View view, String input) {

            Double mLat = Singleton.getInstance().getmLatitude();
            Double mLng = Singleton.getInstance().getmLongitude();

            String url = "https://maps.googleapis.com/maps/";

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

            googleInterface service = retrofit.create(googleInterface.class);

            Call<AutoComplete> call = service.getNearbyPlacesAutoComplete(mLat + "," + mLng, input);

            call.enqueue(new Callback<AutoComplete>() {
                @SuppressLint({"RestrictedApi", "LongLogTag"})
                @Override
                public void onResponse(Call<AutoComplete> call, Response<AutoComplete> place) {
                        listdataForSearch = new ArrayList<>();
                    for (int i = 0; i < place.body().getPredictions().size(); i++) {
                      //  String mS = place.body().getPredictions().get(i).getDescription();
                        listdataForSearch.add(place.body().getPredictions().get(i));

                        placeIdToCompare = place.body().getPredictions().get(i).getPlaceId();


                        mAdapter = new SearchMapAdapter(listdataForSearch, mFragmentManager, getContext());
                        mRecyclerView.setAdapter(mAdapter);
                        System.out.println("2-------->");
                        compareToUpdateMarkers(placeIdToCompare);


                        mRecyclerView.setVisibility(view.VISIBLE);
                    }
                    Log.e("LIST ID----->", listdataForSearch.toString());

                }

                @Override
                public void onFailure(Call<AutoComplete> call, Throwable t) {
                }
            });
        }

    private void compareToUpdateMarkers( String placeIdToCompare) {
        System.out.println("3-------->");
        for (int i = 0; i < Singleton.getInstance().getArrayList().size(); i++) {
            System.out.println("placeIdToCompare--------> " + placeIdToCompare);
            System.out.println("Place ID -------->" + Singleton.getInstance().getArrayList().get(i).getReference());
            if (placeIdToCompare.equals(Singleton.getInstance().getArrayList().get(i).getReference())) {
                mMap.clear();
                System.out.println("placeIdToCompare--------> " + placeIdToCompare);
                System.out.println("Place ID -------->" + Singleton.getInstance().getArrayList().get(i).getId());
                Double mLatForAll = Singleton.getInstance().getArrayList().get(i).getGeometry().getLocation().getLat();
                Double mLngForAll = Singleton.getInstance().getArrayList().get(i).getGeometry().getLocation().getLng();
                mLatLngForAll = new LatLng(mLatForAll, mLngForAll);
                System.out.println("mLatLngForAll--------> " + mLatLngForAll);
            }

                System.out.println("4-------->" + mLatLngForAll);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(mLatLngForAll);
                markerOptions.snippet(placeIdToCompare);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icorange32));
                mRestoMarker = mMap.addMarker(markerOptions);
        }

        Onclick();
    }


    private void loadMap () {
            mMFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.containermap);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkPermissions();
                FragmentManager mFragmentManager = getFragmentManager();
                FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
                mMFragment = SupportMapFragment.newInstance();
                mFragmentTransaction.replace(R.id.containermap, mMFragment).commit();
            }
            mMFragment.getMapAsync(this);
            CheckGooglePlayServices();
        }

    public boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);

            return false;
        } else {
            return true;
        }
    }

    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(getContext());
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(getActivity(), result,
                        0).show();
            }
            return false;
        }
        return true;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();

            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().isTiltGesturesEnabled();
            mMap.setMyLocationEnabled(true);
            mMap.setMinZoomPreference(5.0f);
            mMap.setMaxZoomPreference(20.0f);
        }

        loadDatawithSingleTon();
    }

    private void loadDatawithSingleTon() {
        initMyPosition();
        initRestaurantPosition();
    }

    private void initRestaurantPosition() {
        for (int i = 0; i < Singleton.getInstance().getArrayList().size(); i++) {
            Double mLatForAll = Singleton.getInstance().getArrayList().get(i).getGeometry().getLocation().getLat();
            Double mLngForAll = Singleton.getInstance().getArrayList().get(i).getGeometry().getLocation().getLng();
            mLatLngForAll = new LatLng(mLatForAll, mLngForAll);


            String placeId = Singleton.getInstance().getArrayList().get(i).getPlaceId();
            markerAllRestaurant(mLatLngForAll, placeId, i );

        }

    }

    private void initMyPosition() {
        Double mLat = Singleton.getInstance().getmLatitude();
        Double mLng = Singleton.getInstance().getmLongitude();

        Log.e("LOC in Map---------", mLat + "/" + mLng);

        mLatLng = new LatLng(mLat, mLng);
        loadMapCamera(mLatLng);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void loadMapCamera(LatLng mLatLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 12));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
    }

    private void markerAllRestaurant(LatLng mLatLngForAll, String placeId, int i) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(mLatLngForAll);
        markerOptions.snippet(placeId);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icorange32));
        mRestoMarker = mMap.addMarker(markerOptions);

        Onclick();
    }


    public void Onclick() {
        mMap.setOnMarkerClickListener(marker -> {
            Intent intent = new Intent(getContext(), subactivity.class);
            String mId = marker.getSnippet();
            intent.putExtra("I", mId);
            getContext().startActivity(intent);
            return false;
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }





}