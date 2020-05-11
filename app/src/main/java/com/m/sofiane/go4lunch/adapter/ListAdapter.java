package com.m.sofiane.go4lunch.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.android.gms.maps.model.LatLng;
import com.m.sofiane.go4lunch.activity.subactivity;
import com.m.sofiane.go4lunch.BuildConfig;
import com.m.sofiane.go4lunch.models.MyChoice;
import com.m.sofiane.go4lunch.models.pojoMaps.OpeningHours;
import com.m.sofiane.go4lunch.models.pojoMaps.Result;
import com.m.sofiane.go4lunch.R;
import com.m.sofiane.go4lunch.services.Singleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * created by Sofiane M. 2020-01-30
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> implements Filterable {


    public static final String URLAPI = "https://maps.googleapis.com/maps/";
    public static final String MAX_WIDTH = "&maxheight=10000";
    public static final String MAX_HEIGHT = "&maxheight=10000";
    public static final String URLPHOTO = "https://maps.googleapis.com/maps/api/place/photo?";
    public static final String DEFAUTPHOTO = "https://urlz.fr/cw4j";
    public static final String KEY = "&key=";
    public static final String PHOTOREF = "&photoreference=" ;
    String API_KEY = BuildConfig.APIKEY;
    Double mLatitude,mLongitude;
    Location mCurrentLocation;
    Boolean mOpen;
    String mPlaceId,mDistance,mNameOfRestaurant,mAdress,UrlPhoto,refPhoto,mLoadAdress;
    List<Result> mData,mDataList;
    Context mContext;
    ArrayList<String> mTest;
    FragmentManager fragmentManager;
    CharSequence mKeyName = "";
    Location location;


    public ListAdapter(ArrayList<Result> mData, Context mContext, FragmentManager fragmentManager, CharSequence mKeyName, ArrayList<String> mTest ) {
        this.mData = mData;
        this.mContext = mContext;
        this.fragmentManager = fragmentManager;
        this.mKeyName = mKeyName;
        this.mTest = mTest;

     //   Log.e("ADAPTER ID", String.valueOf(mTest));
       // Log.e("ADAPTER NAME", String.valueOf(mKeyName));

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recyclerview,viewGroup,false);

        return new ListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder h, int i) {
        nameCalling(h,i);
        adresseCalling(h,i);
        imageCalling(h,i);
        rateCalling(h,i);
        distanceCalling(h,i);
        hoursCalling(h,i);
        idDetailCalling(i);
        countCall(h,i);
        clickAndSendData(h,i);


    }

    private void countCall(ViewHolder h, int i) {
        Map<String, Integer> countMap = new HashMap<>();

        for (String item: mTest) {

            if (countMap.containsKey(item))
                countMap.put(item, countMap.get(item) + 1);
            else
                countMap.put(item, 1);
        }

        Log.e("COUNT MAP", countMap.toString());

        if (countMap.get(mNameOfRestaurant)==null)
        {
            h.R_place_people_count.setText("(" + "0" +")");

        }

        else
            {
                h.R_place_people_count.setText("("+countMap.get(mNameOfRestaurant).toString()+")");
            }





    }

    public void clickAndSendData(ViewHolder h, int i ){

        h.R_button.setOnClickListener(view -> {

            Intent intent= new Intent(mContext, subactivity.class);
            intent.putExtra("I", mData.get(i).getPlaceId());
            mContext.startActivity(intent);

        });

    }

    private void idDetailCalling(int i) {
        mPlaceId = mData.get(i).getPlaceId();
    }

    private void hoursCalling(ViewHolder h, int i) {

        if (mData.get(i).getOpeningHours() != null) {
            if (mOpen = mData.get(i).getOpeningHours().getOpenNow().toString().equals("false")) {
                h.R_openhour.setText(R.string.Close);
                h.R_openhour.setTextColor(Color.RED);
            } else {
                h.R_openhour.setText(R.string.Open);
                h.R_openhour.setTextColor(Color.GREEN);
            }
        } else {
            h.R_openhour.setText("-");
            h.R_openhour.setTextColor(Color.WHITE);
        }
    }

    private void distanceCalling(ViewHolder h, int i) {
        mLatitude = mData.get(i).getGeometry().getLocation().getLat();
        mLongitude = mData.get(i).getGeometry().getLocation().getLng();
        if (mLatitude == null || mLatitude == null)

        {mDistance = "";}
        else {

            Location locationA = new Location("A");
            locationA.setLatitude(mLatitude);
            locationA.setLongitude(mLongitude);

            Double mLat = Singleton.getInstance().getmLatitude();
            Double mLng = Singleton.getInstance().getmLongitude();


            Location locationB = new Location("B");
            locationB.setLatitude(mLat);
            locationB.setLongitude(mLng);

            mDistance =String.valueOf(
                    (Math.round
                            (locationA.distanceTo(locationB))));

            h.R_prox.setText(mDistance+ "m");
        }
    }

    private void rateCalling(ViewHolder h, int i) {
        if (mData.get(i).getRating() != null) {
            h.R_rateTxt.setText(String.valueOf(mData.get(i).getRating()));
            double mRate = mData.get(i).getRating();
            if (mRate<4){
                h.R_rate1.setVisibility(View.INVISIBLE);
            } else if (mRate<2) {
                h.R_rate1.setVisibility(View.INVISIBLE);
                h.R_rate2.setVisibility(View.INVISIBLE);
                h.R_rate3.setVisibility(View.INVISIBLE);
            }}

        else {
            h.R_rate1.setVisibility(View.INVISIBLE) ;
            h.R_rate2.setVisibility(View.INVISIBLE) ;
            h.R_rate3.setVisibility(View.INVISIBLE);}
    }

    private void adresseCalling(@NonNull ListAdapter.ViewHolder h, int i ) {
        mAdress = mData.get(i).getVicinity();
        if (mAdress == null)
        {mLoadAdress = "";}
        else {
            mLoadAdress = mAdress.split("\\,", 2)[0];
            h.R_adress.setText(mLoadAdress);
        }
    }

    private void nameCalling(@NonNull ListAdapter.ViewHolder h, int i ) {
        mNameOfRestaurant = mData.get(i).getName();
        if (mNameOfRestaurant == null) {
            mNameOfRestaurant = "";
        } else {
            h.R_name.setText(mNameOfRestaurant);
        }


    }

    private void imageCalling(@NonNull ListAdapter.ViewHolder h, int i) {
        if (mData.get(i).getPhotos().isEmpty())
        {UrlPhoto = DEFAUTPHOTO;}
        else { refPhoto = mData.get(i).getPhotos().get(0).getPhotoReference();

            UrlPhoto = URLPHOTO
                    + MAX_WIDTH
                    + MAX_HEIGHT
                    + PHOTOREF
                    + refPhoto
                    + KEY
                    + API_KEY;

            System.out.println(UrlPhoto);

            RequestManager glide = Glide.with(mContext);
            if (!(UrlPhoto == null)) {
                glide.load(UrlPhoto).into(h.R_photo);
            } else {
                glide.load(R.drawable.ic_notifications_black_24dp).into(h.R_photo);
            }}



    }
    @Override
    public int getItemCount() {
        return (mData == null) ? 0 : mData.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence mQuery) {
                String mQueryString = mQuery.toString();
                if (mQueryString.isEmpty()) {
                    mDataList = mData;
                } else {
                    List<Result> filteredList = new ArrayList<>();
                    for (Result name : mData) {
                        if (name.getName().toLowerCase().contains(mQueryString.toLowerCase())) {
                            filteredList.add(name);
                        }
                        mDataList = filteredList;
                    }

                }
                FilterResults results = new FilterResults();
                results.values = mDataList;
                return results;
            }

            @Override
            public void publishResults(CharSequence constraint, FilterResults results1) {
                mData = (List<Result>) results1.values;
                notifyDataSetChanged();
            }
        };

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.place_name)
        TextView R_name;
        @BindView(R.id.place_address)
        TextView R_adress;
        @BindView(R.id.place_photo)
        ImageView R_photo;
        @BindView(R.id.place_rating_icon1)
        ImageView R_rate1;
        @BindView(R.id.place_rating_icon2)
        ImageView R_rate2;
        @BindView(R.id.place_rating_icon3)
        ImageView R_rate3;
        @BindView(R.id.RateTxt)
        TextView R_rateTxt;
        @BindView(R.id.place_distance)
        TextView R_prox;
        @BindView(R.id.place_open)
        TextView R_openhour;
        @BindView(R.id.buttonForClick)
        ImageButton R_button;
        @BindView(R.id.place_people_count)
        TextView R_place_people_count;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

    }
}