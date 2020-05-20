package com.m.sofiane.go4lunch.services;

import com.m.sofiane.go4lunch.models.pojoMaps.Result;

import java.util.ArrayList;

public final class latAndLngSingleton {
            private static latAndLngSingleton uniqLatandLng;
            public double mLatitude;
            public double mLongitude;

            public latAndLngSingleton() {
            }
            public static latAndLngSingleton getInstance() {
                if (uniqLatandLng == null)
                    uniqLatandLng = new latAndLngSingleton();
                return uniqLatandLng;
            }

    public void setLatitude(double mLatitude)
    {
        this.mLatitude = mLatitude;
    }

    public double getmLatitude()
    {
        return this.mLatitude;
    }

    public void setLongitude(double mLongitude)
    {
        this.mLongitude = mLongitude;
    }

    public double getmLongitude()
    {
        return this.mLongitude;
    }

}