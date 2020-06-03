package com.m.sofiane.go4lunch.services;

public final class LatAndLngSingleton {
    private static LatAndLngSingleton uniqLatandLng;
    public double mLatitude;
    public double mLongitude;

    public LatAndLngSingleton() {
    }

    public static LatAndLngSingleton getInstance() {
        if (uniqLatandLng == null)
            uniqLatandLng = new LatAndLngSingleton();
        return uniqLatandLng;
    }

    public void setLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getmLatitude() {
        return this.mLatitude;
    }

    public void setLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public double getmLongitude() {
        return this.mLongitude;
    }

}