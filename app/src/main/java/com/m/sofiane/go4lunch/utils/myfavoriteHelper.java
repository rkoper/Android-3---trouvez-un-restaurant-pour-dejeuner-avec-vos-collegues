package com.m.sofiane.go4lunch.utils;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.m.sofiane.go4lunch.models.MyChoice;

import java.util.Map;

public class myfavoriteHelper {

    public static CollectionReference getMyFavoriteCollection(){
        String mProfilName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        return FirebaseFirestore.getInstance().collection("MyFavorite").document(mProfilName).collection("MyFavoriteList");
    }

    // --- CREATE ---

    public static DocumentReference createMyFavorite(  String mNameOfResto) {
        return myfavoriteHelper.getMyFavoriteCollection().document(mNameOfResto);
    }

    // --- READ ---
    public static Task<QuerySnapshot> getMyFav(){
        String mProfilName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String mProfilEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Uri mProfilPhoto = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
        return myfavoriteHelper.getMyFavoriteCollection().get();}


    // --- DELETE ---
    public static Task<QuerySnapshot> deleteMyFav(String t){
        return myfavoriteHelper.getMyFavoriteCollection().whereEqualTo("Name", t).get();}



}
