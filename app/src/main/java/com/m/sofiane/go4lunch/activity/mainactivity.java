package com.m.sofiane.go4lunch.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.m.sofiane.go4lunch.R;
import com.m.sofiane.go4lunch.fragment.FavoriteFragment;
import com.m.sofiane.go4lunch.fragment.ListFragment;
import com.m.sofiane.go4lunch.fragment.MapFragment;
import com.m.sofiane.go4lunch.fragment.MyChoiceFragment;
import com.m.sofiane.go4lunch.fragment.SettingsFragment;
import com.m.sofiane.go4lunch.fragment.WorkFragment;
import com.m.sofiane.go4lunch.utils.mychoiceHelper;
import com.m.sofiane.go4lunch.utils.myuserhelper;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;


public class mainactivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.activity_main_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.profiltextnameBis)
    TextView mUserText;
    @BindView(R.id.activity_main_bottom_navigation)
    BottomNavigationView mBottomNavigationView;
    @BindView(R.id.profiltextmailBis)
    TextView mUserMail;
    @BindView(R.id.activity_main_drawer_isOpen)
    NavigationView mNavigationView;

    final Fragment mListFragment = new ListFragment();
   final  Fragment mWorkFragment = new WorkFragment();
    final Fragment mMapFragment = new MapFragment();
    final Fragment mFavFragment = new FavoriteFragment();
    final Fragment mSettingsFragment = new SettingsFragment();
   final Fragment mChoiceFragment = new MyChoiceFragment();

    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = mMapFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragment();
        InitToolBar();
        InitBottomNav(false);
      //  loadFragment(mMapFragment);
        InitDrawerLayout();
        loadUserProfil();
        initFavorite();
        initSettings();
         initMyChoice();
        createFireStoreUser();


    }

    private void initFragment() {
        fm.beginTransaction().add(R.id.fragment_container, mChoiceFragment, "6").hide(mChoiceFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, mSettingsFragment, "5").hide(mSettingsFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, mFavFragment, "4").hide(mFavFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, mWorkFragment, "3").hide(mWorkFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, mListFragment, "2").hide(mListFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container,mMapFragment, "1").commit();
    }


    private void InitDrawerLayout() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        System.out.println("Drawer = " + drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        for (int i = 0; i < mToolbar.getChildCount(); i++) {
            if (mToolbar.getChildAt(i) instanceof ImageButton) {
                mToolbar.getChildAt(i).setScaleX(1f);
                mToolbar.getChildAt(i).setScaleY(1f);
            }
        }
        // initLogOut();
    }

    public void InitBottomNav(boolean isHidden) {
        BottomNavigationView mBottomNavigationView = findViewById(R.id.activity_main_bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
        mBottomNavigationView.setVisibility(isHidden ? View.GONE : View.VISIBLE);
    }


    public void InitToolBar() {

        mToolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        View lltop = findViewById(R.id.LLTop);
        View llbottom = findViewById(R.id.LLbottom);
        lltop.setBackgroundResource(R.drawable.gradientmap);
        llbottom.setBackgroundResource(R.drawable.gradientmap);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mNavigationView = findViewById(R.id.activity_main_drawer_isOpen);
        switch (item.getItemId()) {

            case R.id.map:
                fm.beginTransaction().hide(active).show(mMapFragment).commit();
                active = mMapFragment;
                return true;

            case R.id.list:
                fm.beginTransaction().hide(active).show(mListFragment).commit();
                active = mListFragment;
                return true;

            case R.id.group:
                fm.beginTransaction().hide(active).show(mWorkFragment).commit();
                active = mWorkFragment;
                return true;
        }
        return false;
    }


    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }


    public void loadUserProfil() {
        String mProfilName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String mProfilId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String mProfilEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Uri mProfilPhoto = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();

        NavigationView mNavigationView = findViewById(R.id.activity_main_drawer_isOpen);
        View headerView = mNavigationView.getHeaderView(0);

        TextView navUsername = headerView.findViewById(R.id.profiltextnameBis);
        navUsername.setText(mProfilName);

        TextView navUserMail = (TextView) headerView.findViewById(R.id.profiltextmailBis);
        navUserMail.setText(mProfilEmail);

        ImageView navProfilPic = (ImageView) headerView.findViewById(R.id.profilimage);
        Glide.with(this).load(mProfilPhoto).apply(RequestOptions.circleCropTransform()).into(navProfilPic);

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

        public void createFireStoreUser(){
            String mProfilName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            Uri mProfilPhotoUri = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
            String mProfilPhoto = mProfilPhotoUri.toString();

            Map<String, Object> mDataMap = new HashMap<>();
            mDataMap.put("NameUser", mProfilName);
            mDataMap.put("PhotoUser", mProfilPhoto);

            myuserhelper.createmyuser(mDataMap);


        }


    public void initFavorite(){
        NavigationView mNavigationView = findViewById(R.id.activity_main_drawer_isOpen);
        mNavigationView.getMenu().findItem(R.id.drawer_fav).setOnMenuItemClickListener(menuItem -> {
                    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                    mDrawerLayout.closeDrawers();
                    loadFragment(mFavFragment);

                    return true;
                }
        );}


    public void initMyChoice() {
        NavigationView mNavigationView = findViewById(R.id.activity_main_drawer_isOpen);
        mNavigationView.getMenu().findItem(R.id.drawer_lunch).setOnMenuItemClickListener(menuItem -> {

            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerLayout.closeDrawers();
            loadFragment(mChoiceFragment);

            return true;
        });


    }

    public void initSettings(){
        NavigationView mNavigationView = findViewById(R.id.activity_main_drawer_isOpen);
        mNavigationView.getMenu().findItem(R.id.drawer_settings).setOnMenuItemClickListener(menuItem -> {


                    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                    mDrawerLayout.closeDrawers();
                    loadFragment(mSettingsFragment);

                    return true;
                }
        );}


}




