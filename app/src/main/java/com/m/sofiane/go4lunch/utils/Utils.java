package com.m.sofiane.go4lunch.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.m.sofiane.go4lunch.R;
import com.m.sofiane.go4lunch.activity.subactivity;
import com.m.sofiane.go4lunch.models.NameOfResto;
import com.m.sofiane.go4lunch.models.pojoAutoComplete.AutoComplete;
import com.m.sofiane.go4lunch.models.pojoDetail.Result;
import com.m.sofiane.go4lunch.services.Singleton;
import com.m.sofiane.go4lunch.services.googleInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.m.sofiane.go4lunch.BuildConfig.APIKEY;
import static com.m.sofiane.go4lunch.adapter.ListAdapter.KEY;
import static com.m.sofiane.go4lunch.adapter.ListAdapter.MAX_HEIGHT;
import static com.m.sofiane.go4lunch.adapter.ListAdapter.MAX_WIDTH;
import static com.m.sofiane.go4lunch.adapter.ListAdapter.PHOTOREF;
import static com.m.sofiane.go4lunch.adapter.ListAdapter.URLPHOTO;

public class Utils extends AppCompatActivity {


    public static void colorSearch(SearchView searchView, Toolbar t){
        searchView.setMaxWidth(Integer.MAX_VALUE);
        int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchPlate = searchView.findViewById(searchPlateId);
        searchPlate.setTextColor(t.getResources().getColor(R.color.ColorRed));
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

    public Animation mA (){
        Animation animFadeIn, animFadeOut;
        animFadeIn = AnimationUtils.loadAnimation(this, R.anim.slide_down);

        return animFadeIn;
    }


    public static String urlPhotoForSubactivity(Result mShortCut){
       String  mPhotoN = mShortCut.getPhotos().get(0).getPhotoReference();
       String UrlPhoto = URLPHOTO + MAX_WIDTH + MAX_HEIGHT + PHOTOREF + mPhotoN + KEY + APIKEY;
       return UrlPhoto;
    }

    public static String AdressForSubactivty(Result mShortCut) {
        String mAdressDef;

        if (mShortCut.getAddressComponents().get(0).getLongName() == null || mShortCut.getAddressComponents().get(1).getLongName() == null) {
            mAdressDef = "";
        } else {
            mAdressDef = mShortCut.getAddressComponents().get(0).getLongName() + " " + mShortCut.getAddressComponents().get(1).getLongName();
        }

        return mAdressDef;
    }


    public static String formatAdressForList( String mAdress){
        String mLoadAdress;
        if (mAdress == null)
        {mLoadAdress = "";}
        else {
            mLoadAdress = mAdress.split("\\,", 2)[0];
        }
        return mLoadAdress;
    }


    public static String urlPhotoForList( String refPhoto){
        String UrlPhoto = URLPHOTO + MAX_WIDTH + MAX_HEIGHT + PHOTOREF + refPhoto + KEY + APIKEY;
        return UrlPhoto;
    }

    public static int findrating(double x) {

        double y = (double) (((x / 5) * 3));

        long z =  Math.round(y);

        return Math.round(z);

    }

    public static ArrayList<String> removeDuplicates(ArrayList<String> list) {

        // Create a new ArrayList
        ArrayList<String> newList = new ArrayList<String>();

        // Traverse through the first list
        for (String element : list) {

            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {

                newList.add(element);
            }
        }

        // return the new list
        return newList;
    }

    public static ArrayList<String> readDataFromFirebaseForGreen() {
        String placeIdToCompareMyChoice;
        ArrayList<String> ld;
        ArrayList<String> mListToGreen = null;
        String nameResto;
        String IdResto;
        ld = new ArrayList<>();
        mychoiceHelper.getMyChoice()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            NameOfResto l = document.toObject(NameOfResto.class);
                            ld.add(l.getPlaceID());

                        }
                        System.out.println("-------removeDuplicates---------" + ld);
                    }
                });
        return ld;
    }

}
