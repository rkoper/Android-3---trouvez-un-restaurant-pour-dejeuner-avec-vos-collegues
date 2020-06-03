package com.m.sofiane.go4lunch.utils;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class MyUserHelper {
    public static CollectionReference getMyUserCollection() {
        return FirebaseFirestore.getInstance().collection("MyUser");
    }

    public static DocumentReference createMyUser() {
        String mProfilName = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();
        return MyUserHelper.getMyUserCollection().document(Objects.requireNonNull(mProfilName));
    }

    public static Task<QuerySnapshot> getMyUser() {
        return MyUserHelper.getMyUserCollection().get();
    }

    public static Task<QuerySnapshot> deleteMyUser(String t) {
        return MyUserHelper.getMyUserCollection().whereEqualTo("Name", t).get();
    }

    public static String getProfilName() {
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();
    }

    public static String getProfilPhoto() {
        Uri mProfilImage = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhotoUrl();
        return Objects.requireNonNull(mProfilImage).toString();
    }

    public static String getProfilEmail() {
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
    }
}