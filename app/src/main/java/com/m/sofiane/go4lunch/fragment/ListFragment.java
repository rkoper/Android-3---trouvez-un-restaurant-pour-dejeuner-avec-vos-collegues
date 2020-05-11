package com.m.sofiane.go4lunch.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.m.sofiane.go4lunch.adapter.ListAdapter;
import com.m.sofiane.go4lunch.models.MyChoice;
import com.m.sofiane.go4lunch.models.pojoMaps.Result;
import com.m.sofiane.go4lunch.R;
import com.m.sofiane.go4lunch.services.Singleton;
import com.m.sofiane.go4lunch.utils.mychoiceHelper;

import java.util.ArrayList;

import butterknife.BindView;


public class ListFragment extends Fragment  {


    private RecyclerView mRecyclerView;
    private ArrayList<Result> mData;
    private ListAdapter mAdapter;
    FragmentManager mFragmentManager;
    ArrayList<MyChoice> listData;
    ArrayList<String> mTest;

    @BindView(R.id.List_recyclerView)
    RecyclerView recyclerView;

    String mKeyName = "1";
    private FrameLayout frameLayout;

    private boolean isBackFromB;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, null);
        isBackFromB=false;
        setHasOptionsMenu(true);
        uploadToolbar();
        initRecyclerView();
        this.configureRecyclerView(view);
        return view;
    }


    private void uploadToolbar() {
        TextView mTitleText = getActivity().findViewById(R.id.toolbar_title);
        mTitleText.setText(" I'm Hungry!");

    }

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        Toolbar mToolbar = getActivity().findViewById(R.id.activity_main_toolbar);

        inflater.inflate(R.menu.activity_main_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        setSearchTextColour(searchView);

        searchView.setOnSearchClickListener(v -> mToolbar.setNavigationIcon(null));
        searchView.setOnCloseListener(() -> {
            mToolbar.setNavigationIcon(R.drawable.ic_dehaze_black_24dp);
            initRecyclerView();
            return false;
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("TAG", "onQueryTextSubmitList: "+query );
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() != 0) {
                    mAdapter.getFilter().filter(newText);

                } else {
                    Toolbar mToolbar = getActivity().findViewById(R.id.activity_main_toolbar);
                    mToolbar.setNavigationIcon(R.drawable.ic_dehaze_black_24dp);
                    initRecyclerView();
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setSearchTextColour(SearchView searchView) {
        searchView.setMaxWidth(Integer.MAX_VALUE);
        int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchPlate = searchView.findViewById(searchPlateId);
        searchPlate.setTextColor(getResources().getColor(R.color.Red));
        searchPlate.setBackgroundResource(R.color.tw__solid_white);
    }


    private void configureRecyclerView(View view) {
        this.listData = new ArrayList<>();
        this.mData = new ArrayList<>();
        this.mAdapter = new ListAdapter(this.mData, getContext(), mFragmentManager, mKeyName, mTest );
        mRecyclerView = view.findViewById(R.id.List_recyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

    }

    private void initRecyclerView() {
        ArrayList<Result> mData = Singleton.getInstance().getArrayList();
            readFireBase(mData);

                }

    private void readFireBase(ArrayList<Result> mData) {
        mychoiceHelper.getMyChoice()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            document.getData();
                            MyChoice l = document.toObject(MyChoice.class);
                            listData.add(l); }
                        uploadRecyclerView(mData, listData);
                    }
                });
    }


    private void uploadRecyclerView(ArrayList<Result> mData,  ArrayList<MyChoice> listData ) {
        ArrayList<String> mTest = new ArrayList<>();
        for (int i = 0; i < listData.size(); i++) {
            mTest.add(listData.get(i).getNameOfResto());
            mAdapter = new ListAdapter(mData, getContext(), mFragmentManager, mKeyName, mTest);
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}