package com.m.sofiane.go4lunch.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
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
import com.google.android.libraries.places.api.Places;
import com.m.sofiane.go4lunch.BuildConfig;
import com.m.sofiane.go4lunch.R;
import com.m.sofiane.go4lunch.activity.subactivity;
import com.m.sofiane.go4lunch.adapter.ArrayAdapterSearchView;
import com.m.sofiane.go4lunch.adapter.CustomAdapter;
import com.m.sofiane.go4lunch.models.pojoAutoComplete.AutoComplete;
import com.m.sofiane.go4lunch.models.pojoAutoComplete.Prediction;
import com.m.sofiane.go4lunch.services.Singleton;
import com.m.sofiane.go4lunch.services.googleInterface;

import java.util.ArrayList;

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
    String token = "21051984";
    @BindView(R.id.menu_search)
    MenuItem mSearch;
    ArrayList<Prediction> mT = new ArrayList<>();
    ArrayList<String> mS = new ArrayList<>();
    String input;
    View view;
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    String apiKey = BuildConfig.APIKEY;
    private ArrayAdapterSearchView searchView;
    private LinearLayout recycleLayout;
    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private androidx.appcompat.widget.Toolbar mToolbar1, mToolbar2;
    private FrameLayout frameLayout;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, null);
        ArrayList<String> mT =new ArrayList<>();
        setHasOptionsMenu(true);
        loadMap();
        setRetainInstance(true);
        String apiKey = BuildConfig.APIKEY;
        uploadToolbar();

        return view;
    }

    private void uploadToolbar() {
        TextView mTitleText = (TextView) getActivity().findViewById(R.id.toolbar_title);
        mTitleText.setText(" I'm Hungry!");

    }

    private void initSearch(View view,  ArrayList<String> mT) {

        this.adapter = new CustomAdapter(getContext(), mT);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (!mT.isEmpty()) {
            adapter = new CustomAdapter(getContext(), mT);
            recyclerView.setAdapter(adapter);

            recycleLayout.setVisibility(View.VISIBLE);

            adapter.setOnItemClickListener(item -> searchView.setQuery(item, false));
        } else {
            recycleLayout.setVisibility(View.GONE);
        }

        System.out.println("MT DATA------------>" + mT.toString());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.activity_main_menu_for_map, menu);
        MenuItem searchItem = menu.findItem(R.id.search_menu_item);

        searchView = (ArrayAdapterSearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setLayoutParams(new ActionBar.LayoutParams(Gravity.LEFT));
        searchView.setOnSearchClickListener(v ->
                initQuery());
        searchView.setOnCloseListener(() -> {

            return true;
        });


    }

    public void initQuery(){
        frameLayout.setVisibility(View.VISIBLE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String item) {
                Toast.makeText(getContext(), "QUERY", Toast.LENGTH_SHORT).show();
                if (item.length() != 0) {
                    build_retrofit_and_get_responseForSearch(view, item);}

                else{
                    recyclerView.setVisibility(View.GONE);
                }
                return false;
            }
        });

        searchView.setOnClickListener(v -> {
            String length = searchView.getText();
            searchView.setSelection(length.length());
        });

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
                    ArrayList<String> mT = new ArrayList<>();
                    for (int i = 0; i < place.body().getPredictions().size(); i++) {
                      //  String mS = place.body().getPredictions().get(i).getDescription();
                       mT.add(place.body().getPredictions().get(i).getDescription());


                        initSearch(view, mT);
                    }
                    Log.e("LIST ID----->", mT.toString());

                }

                @Override
                public void onFailure(Call<AutoComplete> call, Throwable t) {
                }
            });
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
            markerAllRestaurant(mLatLngForAll, placeId);

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

    private void markerAllRestaurant(LatLng mLatLngForAll, String placeId) {
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