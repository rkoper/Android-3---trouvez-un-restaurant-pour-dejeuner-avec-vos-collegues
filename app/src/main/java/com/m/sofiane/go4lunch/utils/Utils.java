package com.m.sofiane.go4lunch.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.m.sofiane.go4lunch.R;
import com.m.sofiane.go4lunch.activity.subactivity;

public class Utils extends AppCompatActivity {



    public static void displayStarsforSub(Double mRating, Activity activity){
        ImageView mRate3 =  activity.findViewById(R.id.placeSub_rating_icon3);
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
                mRate1.setVisibility(View.INVISIBLE);
            }
        }
    }

    public static void displayStarsforList(Double mRating, Activity activity){
        ImageView mRate3 =  activity.findViewById(R.id.place_rating_icon1);
        ImageView mRate2 = activity.findViewById(R.id.place_rating_icon1);
        ImageView mRate1 = activity.findViewById(R.id.place_rating_icon1);

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

}
