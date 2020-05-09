package com.m.sofiane.go4lunch.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.m.sofiane.go4lunch.R;
import com.m.sofiane.go4lunch.activity.subactivity;
import com.m.sofiane.go4lunch.models.pojoAutoComplete.Prediction;

import java.util.ArrayList;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


public class SearchMapAdapter extends RecyclerView.Adapter<SearchMapAdapter.ViewHolder> {

    private ArrayList<Prediction> listdataForSearch;
    Context mContext;
    FragmentManager mFragmentManager;

    public SearchMapAdapter(ArrayList<Prediction> listdataForSearch, FragmentManager mFragmentManager, Context mContext) {
        this.listdataForSearch = listdataForSearch;
        this.mFragmentManager = mFragmentManager;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public SearchMapAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_search_map, parent, false);
        return new SearchMapAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int i) {
         String ld = listdataForSearch.get(i).getDescription();
        h.txtname.setText(ld);

        clickAndSendData(h,i);
    }

    private void clickAndSendData(ViewHolder h, int i) {
        h.mButton.setOnClickListener(view -> {
            @SuppressLint("RestrictedApi") Intent intent= new Intent(mContext, subactivity.class);
            intent.putExtra("I", listdataForSearch.get(i).getPlaceId());
            mContext.startActivity(intent);
    });
    }

    @Override
    public int getItemCount() {
        return listdataForSearch.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtname;
        Button mButton;


        public ViewHolder(View itemView) {
            super(itemView);
            txtname = (TextView) itemView.findViewById(R.id.place_name_for_maps);
            mButton = (Button) itemView.findViewById(R.id.button_click_map_search);

        }
    }
}


