package com.m.sofiane.go4lunch.fragment;

import android.Manifest;
import android.content.ClipData;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
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
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.navigation.NavigationView;
import com.m.sofiane.go4lunch.BuildConfig;
import com.m.sofiane.go4lunch.activity.subactivity;
import com.m.sofiane.go4lunch.R;
import com.m.sofiane.go4lunch.services.Singleton;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnItemSelected;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

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

    int AUTOCOMPLETE_REQUEST_CODE = 1;
    String apiKey = BuildConfig.APIKEY;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, null);

        setHasOptionsMenu(true);
        loadMap();
        uploadToolbar();
        setRetainInstance(true);

        String apiKey = BuildConfig.APIKEY;

        if (!Places.isInitialized()) {
            Places.initialize(getContext(), apiKey);
        }

        return view;
    }

    public static LatLng getCoordinate(double lat0, double lng0, long dy, long dx) {
        double lat = lat0 + (180 / Math.PI) * (dy / 6378137);
        double lng = lng0 + (180 / Math.PI) * (dx / 6378137) / Math.cos(lat0);
        return new LatLng(lat, lng);
    }


    private void uploadToolbar() {
        TextView mTitleText = (TextView) getActivity().findViewById(R.id.toolbar_title);
        mTitleText.setText(" I'm Hungry!");
        ImageButton imageButton = getActivity().findViewById(R.id.imageButton);
        imageButton.setVisibility(View.VISIBLE);

        imageButton.setOnClickListener(v -> {
            Toast.makeText(getActivity(),"Hello",Toast. LENGTH_SHORT).show();

        });

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