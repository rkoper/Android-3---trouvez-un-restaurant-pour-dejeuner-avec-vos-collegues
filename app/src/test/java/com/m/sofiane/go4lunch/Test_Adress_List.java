package com.m.sofiane.go4lunch;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Test_Adress_List {
@Test
    public void Test_Adress_List() {
                String mAdressToApi;
                String mAdressToDisplay = "77 Rue Ordener";;
                String mAdressResult;

                mAdressToApi = "77 Rue Ordener, Paris";


    mAdressToDisplay =  mAdressToApi.split("\\,", 2)[0];
    mAdressResult = mAdressToDisplay;
    assertEquals("77 Rue Ordener", mAdressResult);


    }

}

