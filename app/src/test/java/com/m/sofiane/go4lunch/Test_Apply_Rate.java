package com.m.sofiane.go4lunch;

import android.view.View;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class Test_Apply_Rate {
    @Test
    public void Test_Apply_Rate() {
        Double mRating;
        String mDisplay = null;

        mRating = 1.7;

        if (mRating<2)
        {mDisplay = "2 étoiles";}

        assertEquals("2 étoiles", mDisplay);

        }
    }
