package com.m.sofiane.go4lunch.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.m.sofiane.go4lunch.R;
import com.m.sofiane.go4lunch.models.MyChoice;
import com.m.sofiane.go4lunch.utils.MyChoiceHelper;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by Sofiane M. 25/04/2020
 */
public class MyChoiceFragment extends DialogFragment {

    static final String DEFAUTPHOTO = "https://bit.ly/3cIGQsK";
    final Fragment mapFragment = new MapFragment();
    @BindView(R.id.layout_choice_close)
    LinearLayout mLLclose;
    @BindView(R.id.layout_choice_name)
    ConstraintLayout mLLname;
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

    public MyChoiceFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_mychoice, null);
        readFireStore();
        ButterKnife.bind(this, view);
        initCloseButton();
        Window window = Objects.requireNonNull(getDialog()).getWindow();
        Objects.requireNonNull(window).setBackgroundDrawableResource(android.R.color.transparent);
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
        Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mapFragment)
                .addToBackStack(null)
                .commit();
    }

    private void readFireStore() {
        MyChoiceHelper.readMyChoice()
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (Objects.requireNonNull(document).exists()) {
                    MyChoice l = document.toObject(MyChoice.class);

                    String mName = Objects.requireNonNull(l).getNameOfResto();
                    String mAdress = l.getAdress();
                    String mPhotoResto = l.getRestoPhoto();

                    if (mName.equals("0")) {
                        noRestaurantChoice();
                        Log.d("TRUE----------", "get failed with ", task.getException());
                    } else {
                        callMyChoice(mName, mAdress, mPhotoResto);
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }

            }
        });
    }

    private void callMyChoice(String mName, String mAdress, String mPhotoResto) {
        mChoiceName.setText(mName);
        mChoiceAdress.setText(mAdress);
        if (getContext() != null) {
            if (mPhotoResto != null) {
                Glide
                        .with(getContext())
                        .load(mPhotoResto)
                        .apply(RequestOptions.circleCropTransform())
                        .into(mChoicePhoto);
            } else {
                Glide
                        .with(getContext())
                        .load(DEFAUTPHOTO)
                        .apply(RequestOptions.circleCropTransform())
                        .into(mChoicePhoto);
            }
        } else {
            Log.d("TAG", "No such document");
        }
    }

    private void cancelChoice() {
        mCancelChoice.setOnClickListener(v -> {
            MyChoiceHelper.initMyChoice();
            noRestaurantChoice();
        });
    }

    private void noRestaurantChoice() {
        mLLnoChoice.setVisibility(View.VISIBLE);
        mLLphoto.setVisibility(View.INVISIBLE);
        mLLname.setVisibility(View.INVISIBLE);
        mLLcancel.setVisibility(View.INVISIBLE);
    }
}
