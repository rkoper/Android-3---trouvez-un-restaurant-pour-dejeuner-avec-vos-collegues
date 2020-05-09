package com.m.sofiane.go4lunch.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.m.sofiane.go4lunch.R;
import java.util.ArrayList;


public class SearchMapAdapter extends RecyclerView.Adapter<SearchMapAdapter.ViewHolder> {

    private ArrayList<String> listData;
    Context mContext;
    FragmentManager mFragmentManager;

    public SearchMapAdapter(ArrayList<String> listData, FragmentManager mFragmentManager, Context mContext) {
        this.listData = listData;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
         String ld = listData.get(i);
        holder.txtname.setText(ld);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtname;


        public ViewHolder(View itemView) {
            super(itemView);
            txtname = (TextView) itemView.findViewById(R.id.place_name_for_maps);

        }
    }
}


