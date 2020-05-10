package com.m.sofiane.go4lunch.models;

import java.util.ArrayList;

/**
 * created by Sofiane M. 21/04/2020
 */
public  class MyFavorite extends ArrayList<MyFavorite> {

    private String id;
    private String Name;
    private String Adress;
    private String Photo;

    public  MyFavorite(){ }

    public MyFavorite(String Photo, String Name, String Adress) {
        this.Photo = Photo;
        this.Name = Name;
        this.Adress= Adress;
    }

    public String getPhoto() {
        return Photo;
    }

    public String getName() {
        return Name;
    }

    public String getAdress() {
        return Adress;
    }
}

