package com.m.sofiane.go4lunch;

import com.m.sofiane.go4lunch.utils.Utils;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Test_Apply_Rate {
    @Test
    public void Test_Apply_Rate() {
        double x = 3.6;

        int z = Utils.findrating(x);



        assertEquals(2, z);



        }
    }
