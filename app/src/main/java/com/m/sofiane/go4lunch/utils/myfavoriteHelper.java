package com.m.sofiane.go4lunch.utils;

import android.content.Context;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.m.sofiane.go4lunch.R;
import com.m.sofiane.go4lunch.services.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class myfavoriteHelper extends AppCompatActivity {

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
