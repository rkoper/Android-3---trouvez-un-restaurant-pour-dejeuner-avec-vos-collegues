package com.m.sofiane.go4lunch.utils;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MyChoiceHelper {

    public static CollectionReference getMyCHoiceCollection() {
        return FirebaseFirestore.getInstance().collection("MyChoice");
    }

    public static Task<Void> initMyChoice() {
        Map<String, Object> mDataMapForFav = new HashMap<>();
        mDataMapForFav.put("NameOfResto", "0");
        mDataMapForFav.put("PlaceID", "0");
        mDataMapForFav.put("UserName", MyUserHelper.getProfilName());
        mDataMapForFav.put("UserPhoto", MyUserHelper.getProfilPhoto());
        mDataMapForFav.put("RestoPhoto", "0");
        mDataMapForFav.put("Adress", "0");
        mDataMapForFav.put("Id", "2");
        String mProfilName = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();
        return MyChoiceHelper.getMyCHoiceCollection().document(Objects.requireNonNull(mProfilName)).set(mDataMapForFav);
    }

    public static Task<Void> createMyChoice(Map<String, Object> mDataMap) {
        String mProfilName = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();
        return MyChoiceHelper.getMyCHoiceCollection().document(Objects.requireNonNull(mProfilName)).set(mDataMap);
    }

    public static Task<QuerySnapshot> getMyChoice() {
        return MyChoiceHelper.getMyCHoiceCollection().get();
    }

    public static DocumentReference readMyChoice() {
        String mProfilName = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();
        return MyChoiceHelper.getMyCHoiceCollection().document(Objects.requireNonNull(mProfilName));
    }

    public static Task<Void> deleteMyChoice(String mProfilName) {
        return MyChoiceHelper.getMyCHoiceCollection().document(mProfilName).delete();
    }
}
