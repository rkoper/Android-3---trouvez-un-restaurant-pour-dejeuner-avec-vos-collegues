package com.m.sofiane.go4lunch.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * created by Sofiane M. 27/04/2020
 */
public class NameOfResto extends ArrayList<NameOfResto> {

    public NameOfResto (){ }

    private String PlaceID;
    private String NameOfResto;
    private String UserName;
    private String UserPhoto;
    private String Id;



    public NameOfResto(String PlaceID, String NameOfResto, String UserName, String UserPhoto, String Id) {
        this.PlaceID = PlaceID;
        this.NameOfResto = NameOfResto;
        this.UserName= UserName;
        this.UserPhoto= UserPhoto;
        this.Id = Id;

    }

    public String getPlaceID() {
        return PlaceID;
    }

    public void setPlaceID(String placeID) {
        PlaceID = placeID;
    }

    public String getNameOfResto() {
        return NameOfResto;
    }

    public void setNameOfResto(String nameOfResto) {
        NameOfResto = nameOfResto;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserPhoto() {
        return UserPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        UserPhoto = userPhoto;
    }

    public String  getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}