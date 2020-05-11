package com.m.sofiane.go4lunch.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.m.sofiane.go4lunch.R;
import com.m.sofiane.go4lunch.activity.mainactivity;
import com.m.sofiane.go4lunch.models.NameOfResto;

import java.util.ArrayList;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

/**
 * created by Sofiane M. 25/04/2020
 */
public class SubAdapter extends RecyclerView.Adapter<SubAdapter.ViewHolder> {
    public mainactivity activity;
    ArrayList<NameOfResto> listDataName;
    ArrayList listDataPhoto;
    Context mContext;
    FragmentManager mFragmentManager;

    public SubAdapter(ArrayList<NameOfResto> listDataName, ArrayList listDataPhoto, FragmentManager mFragmentManager, Context mContext) {
        this.listDataName = listDataName;
        this.listDataPhoto = listDataPhoto;
        this.mFragmentManager = mFragmentManager;
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_sub, parent, false);

        return new SubAdapter.ViewHolder(view);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int i) {
                h.txtname.setText(listDataName.get(i).getUserName() + " GO GO GO");

        Glide
                .with(getApplicationContext())
                .load(listDataName.get(i).getUserPhoto())
                .apply(RequestOptions.circleCropTransform())
                .into(h.urlphoto);


    }

    @Override
    public int getItemCount() {
        return listDataName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtname;
        ImageView urlphoto;
        Button mButton;


        public ViewHolder(View itemView) {
            super(itemView);
            txtname = itemView.findViewById(R.id.sub_name_profilgo);
            urlphoto = itemView.findViewById(R.id.sub_photo_profilgo);
            mButton = itemView.findViewById(R.id.buttonClick);

        }
    }


}
