package com.m.sofiane.go4lunch.utils;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class MyFavoriteHelper extends AppCompatActivity {

    public static CollectionReference getMyFavoriteCollection() {
        String mProfilName = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();
        return FirebaseFirestore.getInstance().collection("MyFavorite").document(Objects.requireNonNull(mProfilName)).collection("MyFavoriteList");
    }

    public static DocumentReference createMyFavorite(String mNameOfResto) {
        return MyFavoriteHelper.getMyFavoriteCollection().document(mNameOfResto);
    }

    public static Task<QuerySnapshot> getMyFav() {
        return MyFavoriteHelper.getMyFavoriteCollection().get();
    }

    public static Task<QuerySnapshot> deleteMyFav(String t) {
        return MyFavoriteHelper.getMyFavoriteCollection().whereEqualTo("Name", t).get();
    }
}
