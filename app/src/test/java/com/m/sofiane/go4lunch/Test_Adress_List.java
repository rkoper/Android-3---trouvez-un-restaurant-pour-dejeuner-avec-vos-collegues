package com.m.sofiane.go4lunch;

import com.m.sofiane.go4lunch.utils.Utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Test_Adress_List {
    @Test
    public void Test_Adress_List() {
        String mAdressToDisplay = "77 Rue Ordener";
        String mAdressAPI = "77 Rue Ordener, Paris";

        mAdressToDisplay = Utils.formatAdressForList(mAdressAPI);
        assertEquals("77 Rue Ordener", mAdressToDisplay);

    }

}

