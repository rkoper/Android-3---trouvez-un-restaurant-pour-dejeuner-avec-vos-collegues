package com.m.sofiane.go4lunch.utils;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class mychoiceHelper {

    public static CollectionReference getMyCHoiceCollection(){
        return FirebaseFirestore.getInstance().collection("MyChoice");
    }

    // --- CREATE ---

    public static Task<Void> createMyChoice(  Map<String, Object> mDataMap) {
        String mProfilName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        return mychoiceHelper.getMyCHoiceCollection().document(mProfilName).set(mDataMap);
    }

    // --- READ ---

    public static Task<QuerySnapshot> getMyChoice(){
        return mychoiceHelper.getMyCHoiceCollection().get();
    }

    public static DocumentReference readMyChoice() {
        String mProfilName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        return mychoiceHelper.getMyCHoiceCollection().document(mProfilName);
    }

    // --- DELETE ---

    public static Task<Void> deleteMyChoice(String mProfilName) {
        return mychoiceHelper.getMyCHoiceCollection().document(mProfilName).delete();
    }
}
