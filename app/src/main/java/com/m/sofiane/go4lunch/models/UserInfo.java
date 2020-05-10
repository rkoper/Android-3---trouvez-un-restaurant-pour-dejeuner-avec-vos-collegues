package com.m.sofiane.go4lunch.models;

import java.util.ArrayList;

/**
 * created by Sofiane M. 25/04/2020
 */
public class UserInfo extends ArrayList<UserInfo> {


        private String NameUser;
        private String PhotoUser;
        private String IDUser;

        public  UserInfo(){ }

        public UserInfo(String PhotoUser, String NameUser,String IDUser) {
            this.PhotoUser = PhotoUser;
            this.NameUser= NameUser;
            this.IDUser =IDUser;
        }

    public String getNameUser() {
        return NameUser;
    }

    public void setNameUser(String nameUser) {
        NameUser = nameUser;
    }

    public String getPhotoUser() { return PhotoUser; }

    public void setPhotoUser(String photoUser) {
        PhotoUser = photoUser;
    }

    public String getIdUser() { return IDUser; }

    public void setIdUser(String idUser) {
        IDUser = idUser;
    }
}
