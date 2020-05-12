package com.m.sofiane.go4lunch.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.m.sofiane.go4lunch.R;
import com.m.sofiane.go4lunch.models.MyChoice;
import com.m.sofiane.go4lunch.utils.mychoiceHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.firebase.ui.auth.AuthUI.TAG;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;

/**
 * created by Sofiane M. 25/04/2020
 */
public class MyChoiceFragment extends DialogFragment {

    public MyChoiceFragment() { }


    @BindView(R.id.layout_choice_close)
    LinearLayout mLLclose;
    @BindView(R.id.layout_choice_name)
    LinearLayout mLLname;
    @BindView(R.id.layout_choice_photo)
    LinearLayout mLLphoto;
    @BindView(R.id.layout_choice_title)
    LinearLayout mLLtitle;
    @BindView(R.id.frag_choice_title)
    TextView mChoiceTitle;
    @BindView(R.id.layout_choice_no_choice)
    LinearLayout mLLnoChoice;
    @BindView(R.id.frag_choice_no_choice)
    TextView mChoiceNoChoice;
    @BindView(R.id.frag_choice_name)
    TextView mChoiceName;
    @BindView(R.id.frag_choice_photo)
    ImageView mChoicePhoto;
    @BindView(R.id.frag_choice_adress)
    TextView mChoiceAdress;
    @BindView(R.id.layout_mychoice_cancel)
    LinearLayout mLLcancel;
    @BindView(R.id.frag_choice_close)
    ImageButton mChoiceClose;
    @BindView(R.id.image_b_cancel_choice)
    ImageButton mCancelChoice;

    final Fragment mapFragment = new MapFragment();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_mychoice, null);
        readFireStore(view);
        ButterKnife.bind(this, view);
        initCloseButton();
        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        cancelChoice();
        return view;
    }

    private void initCloseButton() {
        mChoiceClose.setOnClickListener(v -> {
            onDestroyView();
            backtoHome();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void backtoHome() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mapFragment)
                .addToBackStack(null)
                .commit();
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

                if (mName.equals("0"))
                {
                    noRestaurantChoice();
                }
                else {
                    callMyChoice(mName, mAdress, mPhotoResto);
                 }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());}

            }
        });
    }

    private void callMyChoice(String mName, String mAdress, String mPhotoResto) {
        mChoiceName.setText(mName);
        mChoiceAdress.setText(mAdress);
        if (getContext() != null){
            Glide
                    .with(getContext())
                    .load(mPhotoResto)
                    .apply(RequestOptions.circleCropTransform())
                    .into(mChoicePhoto);}
        else {
            Log.d("TAG", "No such document");
        }
    }

    private void cancelChoice() {
        mCancelChoice.setOnClickListener(v -> {
            mychoiceHelper.initMyChoice();
            noRestaurantChoice();
        });
    }

    private void noRestaurantChoice(){
        mLLnoChoice.setVisibility(View.VISIBLE);
        mLLphoto.setVisibility(View.INVISIBLE);
        mLLname.setVisibility(View.INVISIBLE);
        mLLcancel.setVisibility(View.INVISIBLE);
    }


}
