package com.m.sofiane.go4lunch.services;

import com.m.sofiane.go4lunch.BuildConfig;
import com.m.sofiane.go4lunch.models.pojoAutoComplete.AutoComplete;
import com.m.sofiane.go4lunch.models.pojoAutoComplete.Prediction;
import com.m.sofiane.go4lunch.models.pojoMaps.Result;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * created by Sofiane M. 2020-02-12
 */
public interface googleInterface {
    String api_KEY = BuildConfig.APIKEY;
     String mUrl = "api/place/nearbysearch/json?&sessiontoken=19840521&key=";
    String mUrlDetail = "api/place/details/json?fields=photos,international_phone_number,name,address_components,formatted_address,rating,website,place_id&sessiontoken=19840521&key=";
    String mUrlAutoComplete = "api/place/autocomplete/json?types=establishment&fields=name,place_id&strictbounds&radius=50&sessiontoken=19840521&key=";


    @GET(mUrl+api_KEY)
    Call<Result>
   getNearbyPlaces(@Query("location") String location, @Query("radius") int radius, @Query("type") String type);

    @GET(mUrlAutoComplete+api_KEY)
    Call<AutoComplete>
    getNearbyPlacesAutoComplete(@Query("location") String location, @Query("input") String input);

    @GET(mUrlAutoComplete+api_KEY)
    Call<Prediction>
    getNearbyPlacesAutoCompleteForList(@Query("location") String location, @Query("input") String input );

    @GET(mUrlDetail+api_KEY)
    Call<com.m.sofiane.go4lunch.models.pojoDetail.Result> getNearbyPlacesDétail(@Query("place_id") String id);

}