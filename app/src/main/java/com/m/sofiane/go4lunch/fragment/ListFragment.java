package com.m.sofiane.go4lunch.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.m.sofiane.go4lunch.adapter.ListAdapter;
import com.m.sofiane.go4lunch.BuildConfig;
import com.m.sofiane.go4lunch.models.MyChoice;
import com.m.sofiane.go4lunch.models.pojoMaps.Result;
import com.m.sofiane.go4lunch.R;
import com.m.sofiane.go4lunch.services.googleInterface;
import com.m.sofiane.go4lunch.utils.mychoiceHelper;
import com.m.sofiane.go4lunch.utils.searchImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class ListFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, searchImpl {

    GoogleApiClient mGoogleApiClient;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private int PROXIMITY_RADIUS = 30;
    private RecyclerView mRecyclerView;
    private ArrayList<Result> mData;
    private ListAdapter mAdapter;;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    Double mLatitude,mLongitude;
    Location mCurrentLocation;
    FragmentManager mFragmentManager;
    ArrayList<MyChoice> listData;
    ArrayList<String> mTest;



    @BindView(R.id.List_recyclerView)
    RecyclerView recyclerView;

    String mKeyName = "1";

    private boolean isBackFromB;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, null);
        isBackFromB=false;
        setHasOptionsMenu(true);
        uploadToolbar();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions();
        }

        this.configureRecyclerView(view);
        buildGoogleApiClient();
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
                Log.e("TAG", "onQueryTextSubmitList: "+query );
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("TAG", "onQueryTextChangeList: "+newText );
                mAdapter.getFilter().filter(newText);
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


    private void configureRecyclerView(View view) {
        this.listData = new ArrayList<>();
        this.mData = new ArrayList<>();
        this.mAdapter = new ListAdapter(this.mData, getContext(), mCurrentLocation, mFragmentManager, mKeyName, mTest );
        mRecyclerView = view.findViewById(R.id.List_recyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

    }

    public boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);

            return false;
        } else {
            return true;
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(Objects.requireNonNull(getContext()))
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();

        mCurrentLocation = mLastLocation;

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

        Call<Result> call = service.getNearbyPlacesForList(mLatitude + "," + mLongitude, PROXIMITY_RADIUS, type);

        call.enqueue(new Callback<Result>() {
            @SuppressLint({"RestrictedApi", "LongLogTag"})
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                ArrayList<Result> mData = response.body().getList();

                mychoiceHelper.getMyChoice()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    document.getData();
                                    MyChoice l = document.toObject(MyChoice.class);

                                    listData.add(l);

                                    }
                                }
                            ArrayList<String> mTest = new ArrayList<>();
                            for (int i = 0; i < listData.size(); i++) {
                                mTest.add(listData.get(i).getNameOfResto());
                                Log.e("TEEEEEET", mTest.toString());

                                mAdapter = new ListAdapter(mData, getContext(), mCurrentLocation, mFragmentManager, mKeyName, mTest);
                                mRecyclerView.setAdapter(mAdapter);
                            }
                        });
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });
    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void searchQueryMapSubmit(String val) {
    }

    @Override
    public void searchQueryMapChanges(String val) {
    }

    @Override
    public void searchQueryListSubmit(String val) {

    }

    @Override
    public void searchQueryListChanges(String val) {

    }
}