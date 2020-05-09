package com.m.sofiane.go4lunch.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.m.sofiane.go4lunch.R;
import com.m.sofiane.go4lunch.fragment.FavoriteFragment;
import com.m.sofiane.go4lunch.fragment.ListFragment;
import com.m.sofiane.go4lunch.fragment.MapFragment;
import com.m.sofiane.go4lunch.fragment.MyChoiceFragment;
import com.m.sofiane.go4lunch.fragment.SettingsFragment;
import com.m.sofiane.go4lunch.fragment.WorkFragment;
import com.m.sofiane.go4lunch.models.pojoMaps.Result;
import com.m.sofiane.go4lunch.services.Singleton;
import com.m.sofiane.go4lunch.services.googleInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.m.sofiane.go4lunch.R.string.navigation_drawer_close;


public class mainactivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.activity_main_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.activity_main_bottom_navigation)
    BottomNavigationView mBottomNavigationView;
    @BindView(R.id.activity_main_drawer_isOpen)
    NavigationView mNavigationView;

    RecyclerView recyclerView;

    final Fragment mapFragment = new MapFragment();
    final Fragment listFragment = new ListFragment();
    final Fragment workFragment = new WorkFragment();
    final Fragment mFavFrag = new FavoriteFragment();
    final Fragment mSettingsFrag = new SettingsFragment();
    final Fragment mChoiceFrag = new MyChoiceFragment();

    String mProfilName, mProfilPhoto, mProfilId, mProfilEmail;
    Uri mProfilPhotoUri;

    Location gps_loc;
    Location network_loc;
    Location final_loc;
    double longitude;
    double latitude;

    final FragmentManager fm = getSupportFragmentManager();
  //  Fragment active = mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        findLocation();

        setContentView(R.layout.activity_main);
    //    fm.beginTransaction().add(R.id.fragment_container, mapFragment, "1").commit();

        ButterKnife.bind(this);

        InitBottomNav(false);
        InitDrawerLayout();
        retrieveUserData();
        createFireStoreUser();


        loadFragment(mapFragment);

    }

    private void findLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        try {

            gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            network_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (gps_loc != null) {
            final_loc = gps_loc;
            latitude = final_loc.getLatitude();
            longitude = final_loc.getLongitude();
        }
        else if (network_loc != null) {
            final_loc = network_loc;
            latitude = final_loc.getLatitude();
            longitude = final_loc.getLongitude();
        }
        else {
            latitude = 0.0;
            longitude = 0.0;
        }

        Log.e("LOC IN MAIN------>", latitude+"/"+longitude );

        Singleton.getInstance().setLatitude(latitude);
        Singleton.getInstance().setLongitude(longitude);

        build_retrofit_and_get_response(latitude, longitude, "restaurant");

    }

    private void InitDrawerLayout() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, navigation_drawer_close);
        System.out.println("Drawer = " + drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        for (int i = 0; i < mToolbar.getChildCount(); i++) {
            if (mToolbar.getChildAt(i) instanceof ImageButton) {
                mToolbar.getChildAt(i).setScaleX(1f);
                mToolbar.getChildAt(i).setScaleY(1f);
            }
        }
        loadUserProfil();
        initLogout();
        initFavDrawer();
        initSettingsDrawer();
        initMyChoiceDrawer();
    }

    public void InitBottomNav(boolean isHidden) {
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
        mBottomNavigationView.setVisibility(isHidden ? View.GONE : View.VISIBLE);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        View llbottom = findViewById(R.id.LLbottom);
        View lltop = findViewById(R.id.LLTop);
        switch (item.getItemId()) {

            case R.id.map:
                loadFragment(mapFragment);
                lltop.setBackgroundResource(R.drawable.gradientmap);
                llbottom.setBackgroundResource(R.drawable.gradientmap);
                break;

            case R.id.list:
                loadFragment(listFragment);
                lltop.setBackgroundResource(R.drawable.gradientlist);
                llbottom.setBackgroundResource(R.drawable.gradientlist);
                break;

            case R.id.group:
                loadFragment(workFragment);
                lltop.setBackgroundResource(R.drawable.gradientwork);
                llbottom.setBackgroundResource(R.drawable.gradientwork);
                break;

        }
        return true;
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }


    public void loadUserProfil() {
        View headerView = mNavigationView.getHeaderView(0);

        TextView navUsername = headerView.findViewById(R.id.profiltextnameBis);
        TextView navUserMail = (TextView) headerView.findViewById(R.id.profiltextmailBis);
        ImageView navProfilPic = (ImageView) headerView.findViewById(R.id.profilimage);

        navUsername.setText(mProfilName);
        navUserMail.setText(mProfilEmail);
        Glide.with(this).load(String.valueOf(mProfilPhotoUri)).apply(RequestOptions.circleCropTransform()).into(navProfilPic);


    }

    private void initSettingsDrawer() {
        mNavigationView.getMenu().findItem(R.id.drawer_settings).setOnMenuItemClickListener(menuItem -> {
            fm.beginTransaction().replace(R.id.fragment_container, mSettingsFrag).show(mSettingsFrag).commit();
            //   active = mSettingsFrag;
            mDrawerLayout.closeDrawers();
            return false;
        });
    }

    private void initMyChoiceDrawer() {
        mNavigationView.getMenu().findItem(R.id.drawer_lunch).setOnMenuItemClickListener(menuItem -> {
            Log.e("TEST---------", "CHOICE");
            fm.beginTransaction().replace(R.id.fragment_container, mChoiceFrag).commit();
            //active = mChoiceFrag;
            mDrawerLayout.closeDrawers();
            return false;
        });
    }

    private void initFavDrawer() {
        mNavigationView.getMenu().findItem(R.id.drawer_fav).setOnMenuItemClickListener(menuItem -> {
            Log.e("TEST---------", "SETTINGS");
            fm.beginTransaction().replace(R.id.fragment_container, mFavFrag).commit();
          //  active = mFavFrag;
            mDrawerLayout.closeDrawers();
            return true;
        });
    }

    private void initLogout() {
        mNavigationView.getMenu().findItem(R.id.drawer_logout).setOnMenuItemClickListener(menuItem -> {

            AuthUI.getInstance()
                    .signOut(mainactivity.this)
                    .addOnCompleteListener(task -> {
                        Intent intent = new Intent(this, loginactivity.class);
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> Toast.makeText(mainactivity.this, "Test3 ", Toast.LENGTH_LONG).show());

            return true;
        });
    }

    public void retrieveUserData() {
        mProfilName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        mProfilPhotoUri = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
        mProfilPhoto = mProfilPhotoUri.toString();
        mProfilId = FirebaseAuth.getInstance().getUid();
        mProfilEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }

    public void createFireStoreUser() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> mDataMap = new HashMap<>();
        mDataMap.put("NameUser", mProfilName);
        mDataMap.put("PhotoUser", mProfilPhoto);

        db.collection("myUser")
                .document("1")
                .collection("myUserList")
                .document(mProfilId)
                .set((mDataMap))
                .addOnSuccessListener(aVoid -> Log.d("TAG", "DocumentSnapshot successfully written!")

                )
                .addOnFailureListener(e -> Log.w("TAG", "Error writing document", e));
    }

    private void build_retrofit_and_get_response(double latitude, double longitude, String type) {

        // ATTENTION
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

        Call<Result> call = service.getNearbyPlaces((latitude+","+longitude), 30, type);

        call.enqueue(new Callback<Result>() {
            @SuppressLint({"RestrictedApi", "LongLogTag"})
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                ArrayList<Result> mArrayList = new ArrayList<>();
              mArrayList = response.body().getList();


                Singleton.getInstance().setArrayList(mArrayList);

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
            } });
    }


}
