package com.m.sofiane.go4lunch.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.m.sofiane.go4lunch.fragment.ListFragment;
import com.m.sofiane.go4lunch.fragment.MapFragment;
import com.m.sofiane.go4lunch.fragment.WorkFragment;

/**
 * created by Sofiane M. 2020-02-11
 */
public class SectionPageAdapter extends FragmentPagerAdapter {

    public SectionPageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    private final MapFragment fragMap = new MapFragment();
    private final ListFragment fragList = new ListFragment();
    private final WorkFragment fragWork = new WorkFragment();

    @NonNull
    @Override
    public Fragment getItem(int i) {
        switch (i) {

            case 0:
                return fragMap;

            case 1:
                return fragList;

            case 2:
                return fragWork;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}