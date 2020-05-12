package com.m.sofiane.go4lunch.utils;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class myuserhelper {
    public static CollectionReference getMyUserCollection(){
        return FirebaseFirestore.getInstance().collection("MyUser");
    }

    // --- CREATE ---

    public static DocumentReference createMyUser() {
        String mProfilName = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();
        return myuserhelper.getMyUserCollection().document(Objects.requireNonNull(mProfilName));
    }

    // --- READ ---
    public static Task<QuerySnapshot> getMyUser(){
        return myuserhelper.getMyUserCollection().get();}



    // --- DELETE ---
    public static Task<QuerySnapshot> deleteMyUser(String t){
        return myuserhelper.getMyUserCollection().whereEqualTo("Name", t).get();}

        public static String getProfilName() {
            return FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }

    public static String getProfilPhoto() {
        Uri mProfilImage = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhotoUrl();
        return mProfilImage.toString();
    }

    public static String getProfilEmail() {
        return FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }
}