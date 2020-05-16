package com.m.sofiane.go4lunch;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Test_Adress_sub {
    @Test
    public void Test_Adress_sub() {
        String mAdressV2;
        String mAdressV3;;
        String mAdressV4;;
        String mAdressToDisplay;
        String mAdressResult;

        mAdressV2 = "77";
        mAdressV3 = ", ";
        mAdressV4 = "Rue Ordener";


        mAdressToDisplay =  mAdressV2 + mAdressV3 + mAdressV4;
        mAdressResult = mAdressToDisplay;
        assertEquals("77, Rue Ordener", mAdressResult);


    }
}
