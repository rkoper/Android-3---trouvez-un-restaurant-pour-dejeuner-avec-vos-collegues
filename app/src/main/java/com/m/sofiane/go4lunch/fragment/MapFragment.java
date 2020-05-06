package com.m.sofiane.go4lunch.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.m.sofiane.go4lunch.activity.subactivity;
import com.m.sofiane.go4lunch.models.pojoAutoComplete.AutoComplete;
import com.m.sofiane.go4lunch.models.pojoAutoComplete.Prediction;
import com.m.sofiane.go4lunch.models.pojoMaps.Result;
import com.m.sofiane.go4lunch.R;
import com.m.sofiane.go4lunch.services.googleInterface;

import java.util.HashMap;

import butterknife.BindView;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapFragment extends Fragment implements  OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {


    int PROXIMITY_RADIUS = 30;
    private SupportMapFragment mMFragment;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    Marker mCurrLocationMarker,mRestoMarker;
    Double mLatitude,mLongitude;
    LatLng mLatLng;
    HashMap<String, LatLng> mHashForLatLng= new HashMap<>();
    HashMap<String, String> mHashForPlaceID= new HashMap<>();

    @BindView(R.id.menu_search)
    MenuItem mSearch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, null);
        setHasOptionsMenu(true);
        loadMap();
        uploadToolbar();
        return view;

    }
    private void uploadToolbar() {
        TextView mTitleText = (TextView) getActivity().findViewById(R.id.toolbar_title);
        mTitleText.setText(" I'm Hungry!");

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        Toolbar mToolbar = (Toolbar) getActivity().findViewById(R.id.activity_main_toolbar);

        inflater.inflate(R.menu.activity_main_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        setSearchTextColour(searchView);

        searchView.setOnSearchClickListener(v -> mToolbar.setNavigationIcon(null));
        searchView.setOnCloseListener(() -> {
            mToolbar.setNavigationIcon(R.drawable.ic_dehaze_black_24dp);
            return false;
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("TAG", "onQueryTextSubmit: "+query );
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("TAG", "onQueryTextChange: "+newText );

                build_retrofit_and_get_responseForSearch(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setSearchTextColour(SearchView searchView) {
        searchView.setMaxWidth(Integer.MAX_VALUE);
        int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchPlate = (EditText) searchView.findViewById(searchPlateId);
        searchPlate.setTextColor(getResources().getColor(R.color.Red));
        searchPlate.setBackgroundResource(R.color.tw__solid_white);
    }


    private void loadMap() {
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
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
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        // mLocationRequest.setInterval(100000);
        // mLocationRequest.setFastestInterval(100000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this::onLocationChanged);

        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", "entered");

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();



        build_retrofit_and_get_response("restaurant");
    }


    private void build_retrofit_and_get_response(String type) {
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

        Call<Result> call = service.getNearbyPlaces(mLatitude + "," + mLongitude, PROXIMITY_RADIUS, type);

        call.enqueue(new Callback<Result>() {
            @SuppressLint({"RestrictedApi", "LongLogTag"})
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                retrieveDataForMarker(response);
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
            } });
    }

    private void build_retrofit_and_get_responseForSearch(String newText) {
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

        Call<AutoComplete> call = service.getNearbyPlacesAutoComplete(mLatitude + "," + mLongitude, newText);

        call.enqueue(new Callback<AutoComplete>() {
            @SuppressLint({"RestrictedApi", "LongLogTag"})
            @Override
            public void onResponse(Call<AutoComplete> call, Response<AutoComplete> place) {
                for (int i = 0; i < place.body().getPredictions().size(); i++) {
                    Prediction mCall = place.body().getPredictions().get(i);
                    String mKeyName = mCall.getTerms().get(0).getValue();
                    Log.e("SEACH API", mKeyName);
                   String mSearchPlaceID = mCall.getPlaceId();


                LatLng mLatLagSearch =  mHashForLatLng.get(mKeyName);

                if (mLatLagSearch==null)
                    return;

                else {
                    Log.e("SEACH API SEACH KEY ", mKeyName );
                    Log.e("SEACH API SEACH LATLNG ", String.valueOf(mLatLagSearch));}

                    markerAllRestaurant(mLatLagSearch, mSearchPlaceID, mKeyName);
                    upDateMarker(mLatLagSearch, mSearchPlaceID, mKeyName);

                }
            }

            @Override
            public void onFailure(Call<AutoComplete> call, Throwable t) {
            } });
    }

    private void retrieveDataForMarker(Response<Result> response) {
        mMap.clear();
        for (int i = 0; i < response.body().getList().size(); i++) {

            Result mCall = response.body().getList().get(i);
            Double lat = mCall.getGeometry().getLocation().getLat();
            Double lng = mCall.getGeometry().getLocation().getLng();
            String placeName = mCall.getName();
            String placeId = mCall.getPlaceId();

            LatLng latLng = new LatLng(lat, lng);

            mHashForLatLng.put(placeName, latLng);
            mHashForPlaceID.put(placeName, placeId);


            if (latLng == null || placeName == null || placeId == null)
            { Toast.makeText(getActivity(), "Data Error", Toast.LENGTH_LONG).show(); }

            markerAllRestaurant(latLng, placeId, placeName);
            markerMyPosition();
            mapCamera();


        }}

    private void mapCamera() {
        mLatLng = new LatLng(mLatitude, mLongitude);
     mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 12));
         mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    private void markerMyPosition() {
        MarkerOptions markerOptions = new MarkerOptions();
        mLatLng = new LatLng(mLatitude, mLongitude);
        markerOptions.position(mLatLng);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icgreen32));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
    }

    private void markerAllRestaurant(LatLng latLng, String placeId, String placename) {
        MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.snippet(placeId);
            markerOptions.title(placename);

            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icorange32));
            mRestoMarker = mMap.addMarker(markerOptions);

            Onclick();
    }

    private void upDateMarker(LatLng latLng, String placeId, String placename){
        mMap.clear();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.snippet(placeId);
        markerOptions.title(placename);

        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icorange32));
        mRestoMarker = mMap.addMarker(markerOptions);

        Onclick();
    }

    public void Onclick () {
        mMap.setOnMarkerClickListener(marker -> {
            mRestoMarker.setVisible(false);
            Intent intent = new Intent(getContext(), subactivity.class);
            String mId = marker.getSnippet();
            intent.putExtra("I", mId);
            getContext().startActivity(intent);
            return false;
        });}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }




}