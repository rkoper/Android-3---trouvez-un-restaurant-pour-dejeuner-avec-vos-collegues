package com.m.sofiane.go4lunch.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.m.sofiane.go4lunch.R;
import com.m.sofiane.go4lunch.models.MyChoice;
import com.m.sofiane.go4lunch.utils.mychoiceHelper;

import java.util.ArrayList;
import java.util.List;

import static com.firebase.ui.auth.AuthUI.TAG;

/**
 * created by Sofiane M. 25/04/2020
 */
public class MyChoiceFragment extends Fragment {
    MyChoiceFragment myChoiceFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_mychoice, null);
        readFireStore(view);

        return view;
    }

    private void readFireStore(View view) {
        mychoiceHelper.readMyChoice()
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    MyChoice l = document.toObject(MyChoice.class);

                    String mName = l.getNameOfResto();
                    String mAdress = l.getAdress();
                    String mPhotoResto = l.getRestoPhoto();

                    TextView nNameChoice = view.findViewById(R.id.MychoiceName);
                    TextView mAdressChoice = view.findViewById(R.id.MychoiceAdress);
                    ImageView imageView = view.findViewById(R.id.MychoicePhoto);

                    nNameChoice.setText(mName);
                    mAdressChoice.setText(mAdress);


                    if (getContext() != null){
                        Glide.with(getContext()).load(mPhotoResto).into(imageView);
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());}

            }
        });
    }
}
