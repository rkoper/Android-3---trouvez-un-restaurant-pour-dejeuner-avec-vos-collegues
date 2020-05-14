package com.m.sofiane.go4lunch;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Test_build_photoURL {
        @Test
        public void Test_build_photoURL() {
            String mURLphoto = "https://maps.googleapis.com/maps/api/place/photo?";
            String mMaxWidth = "&maxheight=10000";
            String mMAxHeight = "&maxheight=10000";
            String mStatPhotoRef = "&photoreference=";
            String mPhotoRef = "CmRaAAAAp3IbeKpad0nPsviscM-p2R-x4xjvWZzlTdEpRWOr2SwoDfQg1V2QCaggBs_nHA4tF5-LSHJSnitYWenQ4pfFEpa46P0mv_xsLl020JLJvBke8zNhTZieIE79yy8ZurV4EhDL6aGuzHMtsuVENBsxJCfKGhQgsqHH3CliLV08wnedGYzEvMTn3A";
            String mStatKEY = "&key=";
            String mKey = "AIzaSyByK0jz-yxjpZFX88W8zjzTwtzMtkPYC4w";
            String mDefUrlPhoto;


            mDefUrlPhoto = mURLphoto + mMaxWidth + mMAxHeight+ mStatPhotoRef + mPhotoRef+ mStatKEY + mKey;
            assertEquals(
                    "https://maps.googleapis.com/maps/api/place/photo?&maxheight=10000&maxheight=10000&photoreference=CmRaAAAAp3IbeKpad0nPsviscM-p2R-x4xjvWZzlTdEpRWOr2SwoDfQg1V2QCaggBs_nHA4tF5-LSHJSnitYWenQ4pfFEpa46P0mv_xsLl020JLJvBke8zNhTZieIE79yy8ZurV4EhDL6aGuzHMtsuVENBsxJCfKGhQgsqHH3CliLV08wnedGYzEvMTn3A&key=AIzaSyByK0jz-yxjpZFX88W8zjzTwtzMtkPYC4w",
                    mDefUrlPhoto);


        }
}
