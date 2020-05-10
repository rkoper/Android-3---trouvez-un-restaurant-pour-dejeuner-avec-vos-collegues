package com.m.sofiane.go4lunch.utils;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class mychoiceHelper {

    public static CollectionReference getMyCHoiceCollection(){
        return FirebaseFirestore.getInstance().collection("MyChoice");
    }

    public static Task<Void> initMyChoice() {
        Map<String, Object> mDataMapForFav = new HashMap<>();
        mDataMapForFav.put("NameOfResto", "0");
        mDataMapForFav.put("PlaceID", "0");
        mDataMapForFav.put("UserName", myuserhelper.getProfilName());
        mDataMapForFav.put("UserPhoto", myuserhelper.getProfilPhoto());
        mDataMapForFav.put("RestoPhoto", "0");
        mDataMapForFav.put("Adress", "0");
        mDataMapForFav.put("Id", "2");
        String mProfilName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        return mychoiceHelper.getMyCHoiceCollection().document(mProfilName).set(mDataMapForFav);
    }

    public static Task<Void> createMyChoice(  Map<String, Object> mDataMap) {
        String mProfilName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        return mychoiceHelper.getMyCHoiceCollection().document(mProfilName).set(mDataMap);
    }

    public static Task<QuerySnapshot> getMyChoice(){
        return mychoiceHelper.getMyCHoiceCollection().get();
    }

    public static DocumentReference readMyChoice() {
        String mProfilName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        return mychoiceHelper.getMyCHoiceCollection().document(mProfilName);
    }

    public static Task<Void> deleteMyChoice(String mProfilName) {
        return mychoiceHelper.getMyCHoiceCollection().document(mProfilName).delete();
    }
}
