package com.m.sofiane.go4lunch.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.m.sofiane.go4lunch.BuildConfig;
import com.m.sofiane.go4lunch.R;
import com.m.sofiane.go4lunch.adapter.SubAdapter;
import com.m.sofiane.go4lunch.models.MyChoice;
import com.m.sofiane.go4lunch.models.NameOfResto;
import com.m.sofiane.go4lunch.models.pojoDetail.Result;
import com.m.sofiane.go4lunch.services.googleInterface;
import com.m.sofiane.go4lunch.utils.mychoiceHelper;
import com.m.sofiane.go4lunch.utils.myfavoriteHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.m.sofiane.go4lunch.adapter.ListAdapter.KEY;
import static com.m.sofiane.go4lunch.adapter.ListAdapter.MAX_HEIGHT;
import static com.m.sofiane.go4lunch.adapter.ListAdapter.MAX_WIDTH;
import static com.m.sofiane.go4lunch.adapter.ListAdapter.PHOTOREF;
import static com.m.sofiane.go4lunch.adapter.ListAdapter.URLAPI;
import static com.m.sofiane.go4lunch.adapter.ListAdapter.URLPHOTO;

/**
 * created by Sofiane M. 2020-04-04
 */
public class subactivity extends AppCompatActivity{

    static final String DEFAUTPHOTO = "https://urlz.fr/cw4j";
    boolean isBackFromB;
    private static final String TAG = "RealtimeDB";
    String mPlaceId, UrlPhoto,mPhone,mSite,mPhotoN,mAdressV2,mAdressV3,mAdressDef,mDisplayNameOfResto;
    Context mContext;
    Double mRating;
    SubAdapter mAdapter;
    RecyclerView mRecyclerView;
    final String APIKEY = BuildConfig.APIKEY;
    ArrayList listDataName,listDataPhoto;
    FragmentManager mFragmentManager;

    SharedPreferences mSharedPreferences;
    public static final String PREFS ="999";
    public static final String FAVSTATUS= "FaveStatus";

    @BindView(R.id.subName)
    TextView mNameSub;
    @BindView(R.id.SubPhoto)
    ImageView mPhotoSub;
    @BindView(R.id.subAdress)
    TextView mAdressSub;
    @BindView(R.id.buttonCall)
    ImageButton mCallButton;
    @BindView(R.id.buttonworld)
    ImageButton mSiteUrl;
    @BindView(R.id.activity_restaurant_web_site_text)
    TextView mSiteView;
    @BindView(R.id.activity_restaurant_call_text)
    TextView mCallView;
    @BindView(R.id.buttonlike)
    ImageButton mLike;
    @BindView(R.id.placeSub_rating_icon1)
    ImageView mRate1;
    @BindView(R.id.placeSub_rating_icon2)
    ImageView mRate2;
    @BindView(R.id.placeSub_rating_icon3)
    ImageView mRate3;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton mActionButton;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subactivity);
        ButterKnife.bind(this);
        isBackFromB=false;
        mPlaceId = getIntent().getStringExtra("I");

        build_retrofit_and_get_response();
        initRV();

       // mLike.setVisibility(View.INVISIBLE);

        mAdapter.notifyDataSetChanged();

    }

    private void initRV( ) {
        this.listDataName = new ArrayList<>();
        this.listDataPhoto = new ArrayList<>();
        mAdapter = new SubAdapter(listDataName, listDataPhoto, mFragmentManager, mContext);
        mRecyclerView = findViewById(R.id.sub_recyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
    }


    private void build_retrofit_and_get_response() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLAPI)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        googleInterface service = retrofit.create(googleInterface.class);
        Call call = service.getNearbyPlacesDetail(mPlaceId);
        call.enqueue(new Callback<Result>() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @SuppressLint("WrongViewCast")
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                assert response.body() != null;
                Result mShortCut = response.body().getListDetail();


                ratingRestaurantCalling(mShortCut);
                photoRestaurantCalling(mShortCut);
                nameRestaurantCalling(mShortCut);
                phoneNumberRestaurantCalling(mShortCut);
                webSiteRestaurantCalling(mShortCut);

                adresseRestaurantCallig(mShortCut);
                clickOnRestaurant();

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });
    }



    private void photoRestaurantCalling(Result mShortCut) {
        if (mShortCut.getPhotos() != null) {
            mPhotoN = mShortCut.getPhotos().get(0).getPhotoReference();
            UrlPhoto = URLPHOTO + MAX_WIDTH + MAX_HEIGHT + PHOTOREF + mPhotoN + KEY + APIKEY;
            System.out.println(UrlPhoto);
            Glide
                    .with(subactivity.this)
                    .load(UrlPhoto).into(mPhotoSub);
        } else {
            Toast.makeText(subactivity.this, "No photo", Toast.LENGTH_LONG).show();
            Glide
                    .with(subactivity.this)
                    .load(DEFAUTPHOTO).into(mPhotoSub);
        }
    }


    private void ratingRestaurantCalling(Result mShortCut) {
        mRating = mShortCut.getRating();
        if (mRating == null) {
            mRate3.setVisibility(View.INVISIBLE);
            mRate2.setVisibility(View.INVISIBLE);
            mRate1.setVisibility(View.INVISIBLE);
        } else {
            if (mRating < 4) {
                mRate3.setVisibility(View.INVISIBLE);
            } else if (mRating < 2) {
                mRate3.setVisibility(View.INVISIBLE);
                mRate2.setVisibility(View.INVISIBLE);
                mRate1.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void adresseRestaurantCallig(Result mShortCut) {
        mAdressV2 = mShortCut.getAddressComponents().get(0).getLongName();
        mAdressV3 = mShortCut.getAddressComponents().get(1).getLongName();

        if (mAdressV2 == null || mAdressV3 == null) {
            mAdressDef = "";
        } else {
            mAdressDef = mAdressV2 + " " + mAdressV3;
            mAdressSub.setText(mAdressDef);
        }
    }

    private void webSiteRestaurantCalling(Result mShortCut) {
        mSite = mShortCut.getWebsite();

        if (mSite == null) {
            mSiteUrl.setAlpha(.3f);
            mSiteUrl.setClickable(false);
            mSiteView.setAlpha(.3f);
        } else {

            mSiteUrl.setOnClickListener(v1 -> {
                if (mSite == null) {
                    Toast.makeText(this, R.string.urlError, Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(this, webviewactivity.class);
                    intent.putExtra("Web", mSite);
                    this.startActivity(intent);
                }
            });
        }
    }


    private void phoneNumberRestaurantCalling(Result mShortCut) {
        mPhone = mShortCut.getInternationalPhoneNumber();
        if (mPhone == null) {
            mCallButton.setClickable(false);
            mCallButton.setEnabled(false);
            mCallButton.setAlpha(.3f);
            mCallView.setAlpha(.3f);
            mCallButton.setClickable(false);
        } else {
            mCallButton.setOnClickListener(v1 -> {
                if (mPhone == null) {
                    Toast.makeText(this, R.string.phoneError, Toast.LENGTH_LONG).show();
                } else {
                    dialContactPhone(mPhone);
                }
            });
        }

    }

    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }

    private void nameRestaurantCalling(Result mShortCut) {
        if (mShortCut == null) {
            mDisplayNameOfResto = "";
        } else {
            mDisplayNameOfResto = mShortCut.getName();
            mNameSub.setText(mDisplayNameOfResto);
        }

        readDataFromFirebase(mDisplayNameOfResto);

        searchFavList(mDisplayNameOfResto);
        searchChoiceList(mDisplayNameOfResto);
    }

    private void searchChoiceList(String mDisplayNameOfResto) {
        mychoiceHelper.readMyChoice()
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (Objects.requireNonNull(document).exists())
                    if (Objects.requireNonNull(document.toObject(MyChoice.class)).getNameOfResto().equals(mDisplayNameOfResto)) {
                        mActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_circle_orange_24dp));
                    } else {
                        mActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_circle_green_24dp));
                    }
                clickOnRestaurant();
            }
        });
    }

    private void searchFavList(String mDisplayNameOfResto) {
        myfavoriteHelper.getMyFav()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> list = new ArrayList<>();
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            list.add(document.getId());
                        }

                        if (list.contains(mDisplayNameOfResto))
                        { mLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_red_full));}
                        LikeRestaurantCalling(mDisplayNameOfResto, list);

                    } else {

                    }
                });
    }

    private void LikeRestaurantCalling(String mDisplayNameOfResto,List<String> list ) {
        mLike.setOnClickListener(v -> {
            if (list.contains(mDisplayNameOfResto))
            { myfavoriteHelper.getMyFavoriteCollection().document(mDisplayNameOfResto).delete().addOnSuccessListener(aVoid -> Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show());

            mLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border_black_24dp));
                finish();
                startActivity(getIntent()); }
            else { mLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_red_full));

                    Map<String, Object> mDataMap = new HashMap<>();
                    mDataMap.put("Name", mDisplayNameOfResto);
                    mDataMap.put("Adress", mAdressDef);
                    mDataMap.put("Photo", UrlPhoto);
                    mDataMap.put("FireStoreID", getTaskId());

                    myfavoriteHelper.createMyFavorite(mDisplayNameOfResto).set(mDataMap);
                    finish();
                    startActivity(getIntent());
                }
        });
    }

    public void clickOnRestaurant() {
        mActionButton.setOnClickListener(v1 -> {
            mActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_circle_orange_24dp));

            String mProfilName = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();
            String mProfilPhoto = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).toString();

            Map<String, Object> mDataMap = new HashMap<>();
            mDataMap.put("NameOfResto", mDisplayNameOfResto);
            mDataMap.put("PlaceID", mPlaceId);
            mDataMap.put("UserName", mProfilName);
            mDataMap.put("UserPhoto", mProfilPhoto);
            mDataMap.put("RestoPhoto", UrlPhoto);
            mDataMap.put("Adress", mAdressDef);
            mDataMap.put("Id", "1");

            mychoiceHelper.createMyChoice(mDataMap);

        });
    }


    private void readDataFromFirebase(String mDisplayNameOfResto) {
        mychoiceHelper.getMyChoice()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            if (mDisplayNameOfResto.equals(document.getData().get("NameOfResto"))) {
                                NameOfResto l = document.toObject(NameOfResto.class);
                                listDataName.add(l);
                            } else {
                                Log.e("no", "match");
                            }

                            mAdapter = new SubAdapter(listDataName, listDataPhoto,  mFragmentManager, mContext);
                            mRecyclerView.setAdapter(mAdapter);
                        }
                    }
                });
    }
    @Override
    protected void onPause(){
        mAdapter.notifyDataSetChanged();
        super.onPause();

    }
    @Override
    protected void onResume() {
        mAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        android.app.FragmentManager manager =getFragmentManager();
        int count = manager.getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
        } else {
            manager.popBackStack();
        }
    }

}