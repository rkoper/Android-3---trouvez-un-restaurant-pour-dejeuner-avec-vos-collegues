package com.m.sofiane.go4lunch.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.m.sofiane.go4lunch.R;
import com.m.sofiane.go4lunch.adapter.WorkAdapter;
import com.m.sofiane.go4lunch.models.NameOfResto;
import com.m.sofiane.go4lunch.utils.MyChoiceHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;


public class WorkFragment extends Fragment {
    @BindView(R.id.menu_search)
    MenuItem mSearch;

    WorkAdapter mAdapter;
    FragmentManager mFragmentManager;
    ArrayList<NameOfResto> listData;
    RecyclerView mRecyclerView;
    Context mContext;
    ArrayList<String> mMatchMap;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work, null);
        uploadToolbar(view);
        this.configureRecyclerView(view);
        readDataFromFirebase();
        return view;
    }

    private void configureRecyclerView(View view) {
        this.listData = new ArrayList<>();
        this.mMatchMap = new ArrayList<>();
        this.mAdapter = new WorkAdapter(listData, mFragmentManager, mContext);
        mRecyclerView = view.findViewById(R.id.Work_recyclerV);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
    }

    private void readDataFromFirebase() {
        String mProfilName = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();
        FirebaseFirestore
                .getInstance()
                .collection("MyFavorite")
                .document(Objects.requireNonNull(mProfilName))
                .collection("MyFavoriteList")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> list = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            list.add(document.getId());
                        }
                        for (int i = 0; i < list.size(); i++) {
                            Log.d("TAG))))))))))))))))", list.get(i));
                        }
                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                });


        MyChoiceHelper.getMyChoice()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Log.d("TAG", document.getId() + " => " + document.getData());
                            NameOfResto l = document.toObject(NameOfResto.class);

                            listData.add(l);

                            Collections.sort(listData, (rhs, lhs) -> {
                                int a = Integer.parseInt(rhs.getId());
                                int b = Integer.parseInt(lhs.getId());
                                return Integer.compare(a, b);
                            });
                        }
                        mAdapter = new WorkAdapter(listData, mFragmentManager, mContext);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                });
    }

    private void uploadToolbar(View view) {
        TextView mTitleText = Objects.requireNonNull(getActivity()).findViewById(R.id.toolbar_title);
        mTitleText.setText(R.string.workmats);

    }

}