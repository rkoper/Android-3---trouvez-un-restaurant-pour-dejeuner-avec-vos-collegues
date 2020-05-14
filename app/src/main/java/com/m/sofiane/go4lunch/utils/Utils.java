package com.m.sofiane.go4lunch.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.m.sofiane.go4lunch.R;
import com.m.sofiane.go4lunch.activity.mainactivity;
import com.m.sofiane.go4lunch.adapter.ListAdapter;
import com.m.sofiane.go4lunch.adapter.SearchMapAdapter;
import com.m.sofiane.go4lunch.models.pojoAutoComplete.AutoComplete;
import com.m.sofiane.go4lunch.services.Singleton;
import com.m.sofiane.go4lunch.services.googleInterface;

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Utils extends AppCompatActivity {


    public static void displayStarsforSub(Double mRating, Activity activity) {
        ImageView mRate3 = activity.findViewById(R.id.placeSub_rating_icon3);
        ImageView mRate2 = activity.findViewById(R.id.placeSub_rating_icon2);
        ImageView mRate1 = activity.findViewById(R.id.placeSub_rating_icon1);

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
            }
        }
    }


    public static String findDistance(Double mLatitude, Double mLongitude) {
        String mDistance;
        if (mLatitude == null || mLongitude == null)

        {mDistance = "";}
        else {
            Location locationA = new Location("A");
            locationA.setLatitude(mLatitude);
            locationA.setLongitude(mLongitude);

            double mLat = Singleton.getInstance().getmLatitude();
            double mLng = Singleton.getInstance().getmLongitude();

            Location locationB = new Location("B");
            locationB.setLatitude(mLat);
            locationB.setLongitude(mLng);
            mDistance = String.valueOf(
                    (Math.round
                            (locationA.distanceTo(locationB))));}

        return mDistance;
    }


    public static void colorSearch(SearchView searchView, Toolbar t){
        searchView.setMaxWidth(Integer.MAX_VALUE);
        int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchPlate = searchView.findViewById(searchPlateId);
        searchPlate.setTextColor(t.getResources().getColor(R.color.Red));
        searchPlate.setBackgroundResource(R.drawable.dialog_rounded);
    }


    public static  Call<AutoComplete> retrofitforMaps(String input) {
        Double mLat = Singleton.getInstance().getmLatitude();
        double mLng = Singleton.getInstance().getmLongitude();

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

        return call;
    }


}
