package com.m.sofiane.go4lunch.fragment;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.m.sofiane.go4lunch.R;
import com.m.sofiane.go4lunch.adapter.FavoriteAdapter;
import com.m.sofiane.go4lunch.models.MyFavorite;
import com.m.sofiane.go4lunch.utils.myfavoriteHelper;

import java.util.ArrayList;
import java.util.List;


public class FavoriteFragment extends Fragment {
    FavoriteAdapter mAdapter;
    List<MyFavorite> listData;
    RecyclerView mRecyclerView;
    Context mContext;
    final Fragment mapFragment = new MapFragment();
    FragmentManager mFragmentManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, null);
        uploadToolbar();
        uploadBototmBr(view);
        this.configureRecyclerView(view);
        readDataFromFirebase();
        return view;
    }

    private void uploadBototmBr(View view) {
        BottomNavigationView mBottomNavigationView = getActivity().findViewById(R.id.activity_main_bottom_navigation);
        mBottomNavigationView.setVisibility(mBottomNavigationView.GONE);
        BottomNavigationView mBmNaViewForDrawer = view.findViewById(R.id.drawer_bottom_navigation);
        mBmNaViewForDrawer.setVisibility(view.VISIBLE);
        mBmNaViewForDrawer.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                //   mBmNaViewForDrawer.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#ff4444")));
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, mapFragment)
                        .addToBackStack(null)
                        .commit();
                System.out.println("BACK = " + "To the future");
            } return true;
        });
    }


    private void uploadToolbar() {
        TextView mTitleText = (TextView) getActivity().findViewById(R.id.toolbar_title);
        mTitleText.setText(" My Favorites");

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.activity_main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void configureRecyclerView(View view) {
        this.listData = new ArrayList<MyFavorite>();
        this.mAdapter = new FavoriteAdapter(this.listData, mFragmentManager, mContext);
        mRecyclerView = view.findViewById(R.id.Fav_recyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
    }


    public void readDataFromFirebase() {
        myfavoriteHelper.getMyFav()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            MyFavorite l = document.toObject(MyFavorite.class);
                            Log.e("My data fav", document.toString());

                            listData.add(l);

                            Log.e("DATA TEST", listData.toString());

                            mAdapter = new FavoriteAdapter(listData, mFragmentManager, mContext);
                            mRecyclerView.setAdapter(mAdapter);
                        }
                    } else {
                        Log.d(" DATA R F", "Error getting documents: ", task.getException());
                    }
                });
    }
}
