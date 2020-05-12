package com.m.sofiane.go4lunch.models;

import java.util.ArrayList;
import java.util.Map;

/**
 * created by Sofiane M. 21/04/2020
 */
public  class MyChoice extends ArrayList<MyChoice> {

    private String PlaceID;
    private String NameOfResto;
    private String Adress;
    private String RestoPhoto;


    public String getRestoPhoto() {
        return RestoPhoto;
    }

    public void setRestoPhoto(String restoPhoto) {
        RestoPhoto = restoPhoto;
    }

    public MyChoice(){ }

    public MyChoice(String placeID, String nameOfResto, String adress, String restoPhoto) {

        PlaceID = placeID;
        NameOfResto = nameOfResto;
        Adress = adress;
        RestoPhoto = restoPhoto;
    }



    ArrayList<String> mNameList = new ArrayList<>();

    public MyChoice(Map<String, Object> myChoiceMap) {
    }

    public void setmNameList(ArrayList<String> mNameList) {
        this.mNameList = mNameList;
    }

    public ArrayList<String> getmNameList() {
        return mNameList;
    }


    public String getNameOfResto() {
        return NameOfResto;
    }

    public String getAdress() {
        return Adress;
    }

    public String getPlaceID() {return PlaceID;}
}

