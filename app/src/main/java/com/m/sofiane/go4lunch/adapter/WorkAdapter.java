package com.m.sofiane.go4lunch.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.m.sofiane.go4lunch.R;
import com.m.sofiane.go4lunch.activity.mainactivity;
import com.m.sofiane.go4lunch.activity.subactivity;
import com.m.sofiane.go4lunch.models.MyChoice;
import com.m.sofiane.go4lunch.models.NameOfResto;
import com.m.sofiane.go4lunch.models.UserInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

/**
 * created by Sofiane M. 26/04/2020
 */
public class WorkAdapter extends RecyclerView.Adapter<WorkAdapter.ViewHolder> {
    public mainactivity activity;
    private ArrayList<NameOfResto> listData;
    Context mContext;
    FragmentManager mFragmentManager;



    public WorkAdapter(ArrayList<NameOfResto> listData, FragmentManager mFragmentManager, Context mContext) {
        this.listData = listData;
        this.mFragmentManager = mFragmentManager;
        this.mContext = mContext;

        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_workgo, parent, false);
        return new WorkAdapter.ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "RestrictedApi"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int i) {

        if (listData.get(i).getNameOfResto().equals("0"))
        {
            h.txtname.setTextColor(0xffbdbdbd);
            h.txtname.setTypeface(h.txtname.getTypeface(), Typeface.BOLD_ITALIC);
            h.txtname.setText(listData.get(i).getUserName() + " haven't decided yet");

        }
        else
            {
              h.txtname.setText(listData.get(i).getUserName() + " eat @ " + listData.get(i).getNameOfResto());
            }

        Glide
                .with(getApplicationContext())
                .load(listData.get(i).getUserPhoto())
                .apply(RequestOptions.circleCropTransform())
                .into(h.urlphoto);

        if (listData.get(i).getPlaceID().equals("0"))
        { }

        else {
            clickOnItem(h, listData.get(i).getPlaceID());}
    }

    @SuppressLint("RestrictedApi")
    private void clickOnItem(ViewHolder h, String mPlaceID) {

        h.mButton.setOnClickListener(view -> {

            @SuppressLint("RestrictedApi") Intent intent= new Intent(getApplicationContext(), subactivity.class);
            intent.putExtra("I", mPlaceID);
            getApplicationContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtname;
        ImageView urlphoto;
        Button mButton;


        public ViewHolder(View itemView) {
            super(itemView);
            txtname = (TextView) itemView.findViewById(R.id.sub_name_profilgo);
            urlphoto = (ImageView) itemView.findViewById(R.id.sub_photo_profilgo);
            mButton = (Button) itemView.findViewById(R.id.buttonClick);

        }
    }


}
