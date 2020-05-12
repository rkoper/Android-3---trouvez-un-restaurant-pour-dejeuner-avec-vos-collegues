package com.m.sofiane.go4lunch.utils;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class myfavoriteHelper {

    public static CollectionReference getMyFavoriteCollection(){
        String mProfilName = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();
        return FirebaseFirestore.getInstance().collection("MyFavorite").document(Objects.requireNonNull(mProfilName)).collection("MyFavoriteList");
    }


    public static DocumentReference createMyFavorite(  String mNameOfResto) {
        return myfavoriteHelper.getMyFavoriteCollection().document(mNameOfResto);
    }



    // --- READ ---
    public static Task<QuerySnapshot> getMyFav(){
        return myfavoriteHelper.getMyFavoriteCollection().get();}



    // --- DELETE ---
    public static Task<QuerySnapshot> deleteMyFav(String t){
        return myfavoriteHelper.getMyFavoriteCollection().whereEqualTo("Name", t).get();}



}
