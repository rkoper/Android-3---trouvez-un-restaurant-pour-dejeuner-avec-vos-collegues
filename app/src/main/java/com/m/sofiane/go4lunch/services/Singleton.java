package com.m.sofiane.go4lunch.services;

import com.m.sofiane.go4lunch.models.pojoMaps.Result;

import java.util.ArrayList;
import java.util.List;

public final class Singleton {
            private static Singleton uniqInstance;
            public ArrayList<Result> results = new ArrayList<>();
            public double mLatitude;
            public double mLongitude;

            public Singleton() {
            }
            public static Singleton getInstance() {
                if (uniqInstance == null)
                    uniqInstance = new Singleton();
                return uniqInstance;
            }

            public void setArrayList(ArrayList<Result> results)
            {
                this.results = results;
            }
            public ArrayList<Result> getArrayList()
            {
                return this.results;

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