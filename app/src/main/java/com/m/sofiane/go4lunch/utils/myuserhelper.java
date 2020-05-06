package com.m.sofiane.go4lunch.utils;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class myuserhelper {
    public static CollectionReference getMyFavoriteCollection(){
        return FirebaseFirestore.getInstance().collection("MyUser");
    }

    // --- CREATE ---

    public static Task<Void> createmyuser(Map<String, Object> mDataMap) {
        String mProfilName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        return myuserhelper.getMyFavoriteCollection().document(mProfilName).set(mDataMap);
    }

}