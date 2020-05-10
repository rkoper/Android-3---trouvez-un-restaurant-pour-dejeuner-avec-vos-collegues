package com.m.sofiane.go4lunch.utils;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class myuserhelper {
    public static CollectionReference getMyUserCollection(){
        return FirebaseFirestore.getInstance().collection("MyUser");
    }

    // --- CREATE ---

    public static DocumentReference createMyUser() {
        String mProfilName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        return myuserhelper.getMyUserCollection().document(mProfilName);
    }

    // --- READ ---
    public static Task<QuerySnapshot> getMyUser(){
        return myuserhelper.getMyUserCollection().get();}



    // --- DELETE ---
    public static Task<QuerySnapshot> deleteMyUser(String t){
        return myuserhelper.getMyUserCollection().whereEqualTo("Name", t).get();}

        public static String getProfilName() {
            String mProfilName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            return mProfilName;
    }

    public static String getProfilPhoto() {
        Uri mProfilImage = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
        String mProfilPhoto = mProfilImage.toString();
        return mProfilPhoto;
    }

    public static String getProfilEmail() {
        String mProfilEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        return mProfilEmail;
    }
}