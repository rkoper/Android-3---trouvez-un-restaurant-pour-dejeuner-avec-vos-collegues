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


    private static final String DEFAUT_PHOTO = "https://www.123-stickers.com/7507-7924-thickbox/sticker-dark-vador-star-wars-profil.jpg";

    Fragment mListFragment = new ListFragment();
    Fragment mWorkFragment = new WorkFragment();
    Fragment mMapFragment = new MapFragment();
    Fragment mFavFragment = new FavoriteFragment();
    Fragment mSettingsFragment = new SettingsFragment();
    Fragment mChoiceFragment = new MyChoiceFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitToolBar(false);
        InitBottomNav(false);
        loadFragment(new MapFragment());
        InitDrawerLayout();
        //Profile profile = Profile.getCurrentProfile();
        //    ButterKnife.bind(this);
        loadUserProfil();
        //  initLogOut();
        initFavorite();
        //   initMessage();
        initSettings();
        initMyChoice();
        createFireStoreUser();

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
        //   mBottomNavigationView.getItemIconSize();
        // mBottomNavigationView.getItemTextAppearanceActive();
        // mBottomNavigationView.getItemTextAppearanceInactive();
        //  mBottomNavigationView.getForegroundGravity();
        // mBottomNavigationView.getTextAlignment();

    }

    public void InitToolBar(boolean isHidden) {

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
        View llbottom = findViewById(R.id.LLbottom);
        View lltop = findViewById(R.id.LLTop);
        switch (item.getItemId()) {

            case R.id.map:
                loadFragment(new MapFragment());
                lltop.setBackgroundResource(R.drawable.gradientmap);
                llbottom.setBackgroundResource(R.drawable.gradientmap);
                break;

            case R.id.list:
                loadFragment(mListFragment);
                lltop.setBackgroundResource(R.drawable.gradientlist);
                llbottom.setBackgroundResource(R.drawable.gradientlist);
                break;

            case R.id.group:
                loadFragment(mWorkFragment);
                lltop.setBackgroundResource(R.drawable.gradientwork);
                llbottom.setBackgroundResource(R.drawable.gradientwork);
                break;

            case R.id.drawer_lunch:
                loadFragment(mChoiceFragment);
                lltop.setBackgroundResource(R.drawable.gradientfull);
                llbottom.setBackgroundResource(R.drawable.gradientfull);
                break;

            case R.id.drawer_fav:
                loadFragment(mFavFragment);
                lltop.setBackgroundResource(R.drawable.gradientfull);
                llbottom.setBackgroundResource(R.drawable.gradientfull);
                break;

            case R.id.drawer_settings:
                loadFragment(mSettingsFragment);
                lltop.setBackgroundResource(R.drawable.gradientfull);
                llbottom.setBackgroundResource(R.drawable.gradientfull);
                break;

        }
        return true;
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
        String mProfilId = FirebaseAuth.getInstance().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> mDataMap = new HashMap<>();
        mDataMap.put("NameUser", mProfilName);
        mDataMap.put("PhotoUser", mProfilPhoto);

        db.collection("myUser")
                .document("1")
                .collection("myUserList")
                .document(mProfilId)
                .set( (mDataMap))
                .addOnSuccessListener(aVoid -> Log.d("TAG", "DocumentSnapshot successfully written!")

                )
                .addOnFailureListener(e -> Log.w("TAG", "Error writing document", e));


    }

    /*
    public void loadUserProfil(Profile profile) {
        String mName = "";
        String mEmail = "";
        Uri mProfilPic;

        GoogleSignInAccount mGoogleData = GoogleSignIn.getLastSignedInAccount(this);

        if (mGoogleData != null) {
            mName = mGoogleData.getGivenName();
            mEmail = mGoogleData.getEmail();
            if (mGoogleData.getPhotoUrl()==null)
            {mProfilPic = Uri.parse(DEFAUT_PHOTO);}
            else {
            mProfilPic = mGoogleData.getPhotoUrl();}

        } else {
            mName = profile.getName();
            mProfilPic = profile.getProfilePictureUri(150, 150);
        }

            NavigationView mNavigationView = (NavigationView) findViewById(R.id.activity_main_drawer_isOpen);
            View headerView = mNavigationView.getHeaderView(0);

            TextView navUsername = (TextView) headerView.findViewById(R.id.profiltextname);
            navUsername.setText(mName);

            TextView navUserMail = (TextView) headerView.findViewById(R.id.profiltextmail);
            navUserMail.setText(mEmail);


            ImageView navProfilPic = (ImageView) headerView.findViewById(R.id.profilimage);
            Glide.with(this).load(mProfilPic).apply(RequestOptions.circleCropTransform()).into(navProfilPic);

         //   writeInFirestoreDataBase();


            });
        }
*/

    public void initFavorite(){
        NavigationView mNavigationView = findViewById(R.id.activity_main_drawer_isOpen);
        mNavigationView.getMenu().findItem(R.id.drawer_fav).setOnMenuItemClickListener(menuItem -> {

                    //  Toast.makeText(this, "ALLo", Toast.LENGTH_LONG).show();
                    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                    mDrawerLayout.closeDrawers();
                    loadFragment(mFavFragment);

                    return true;
                }
        );}


    public void initMyChoice(){
        NavigationView mNavigationView = findViewById(R.id.activity_main_drawer_isOpen);
        mNavigationView.getMenu().findItem(R.id.drawer_lunch).setOnMenuItemClickListener(menuItem -> {

                    //    Toast.makeText(this, "ALLo", Toast.LENGTH_LONG).show();
                    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                    mDrawerLayout.closeDrawers();
                    loadFragment(mChoiceFragment);

                    return true;
                }
        );}

    public void initSettings(){
        NavigationView mNavigationView = findViewById(R.id.activity_main_drawer_isOpen);
        mNavigationView.getMenu().findItem(R.id.drawer_settings).setOnMenuItemClickListener(menuItem -> {

                    //    Toast.makeText(this, "ALLo", Toast.LENGTH_LONG).show();
                    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                    mDrawerLayout.closeDrawers();
                    loadFragment(mSettingsFragment);

                    return true;
                }
        );}

    public  void initMessage(){

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("M1", "getInstanceId failed", task.getException());
                        return;
                    }

                    // Get new Instance ID token
                    String token = task.getResult().getToken();

                    // Log and toast
                    //     String msg = getString("Token: ", token);
                    Log.d("MOK", token);
                    Toast.makeText(this, token, Toast.LENGTH_SHORT).show();
                });
    }
}


