package com.m.sofiane.go4lunch.utils;

import android.widget.EditText;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.m.sofiane.go4lunch.R;
import com.m.sofiane.go4lunch.models.pojoAutoComplete.AutoComplete;
import com.m.sofiane.go4lunch.models.pojoDetail.Result;
import com.m.sofiane.go4lunch.services.GoogleInterface;
import com.m.sofiane.go4lunch.services.LatAndLngSingleton;

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


    public static void colorSearch(SearchView searchView, Toolbar t) {
        searchView.setMaxWidth(Integer.MAX_VALUE);
        int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchPlate = searchView.findViewById(searchPlateId);
        searchPlate.setTextColor(t.getResources().getColor(R.color.ColorRed));
        searchPlate.setBackgroundResource(R.drawable.dialog_rounded);
    }

    public static Call<AutoComplete> retrofitforMaps(String input) {
        double mLat = LatAndLngSingleton.getInstance().getmLatitude();
        double mLng = LatAndLngSingleton.getInstance().getmLongitude();

        String url = "https://maps.googleapis.com/maps/";

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        GoogleInterface service = retrofit.create(GoogleInterface.class);

        Call<AutoComplete> call = service.getNearbyPlacesAutoComplete(mLat + "," + mLng, input);

        return call;
    }

    public static String urlPhotoForSubactivity(Result mShortCut) {
        String mPhotoN = mShortCut.getPhotos().get(0).getPhotoReference();
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

    public static String formatAdressForList(String mAdress) {
        String mLoadAdress;
        if (mAdress == null) {
            mLoadAdress = "";
        } else {
            mLoadAdress = mAdress.split("\\,", 2)[0];
        }
        return mLoadAdress;
    }

    public static String urlPhotoForList(String refPhoto) {
        String UrlPhoto = URLPHOTO + MAX_WIDTH + MAX_HEIGHT + PHOTOREF + refPhoto + KEY + APIKEY;
        return UrlPhoto;
    }

    public static int findrating(double x) {

        double y = ((x / 5) * 3);

        long z = Math.round(y);

        return Math.round(z);

    }

}