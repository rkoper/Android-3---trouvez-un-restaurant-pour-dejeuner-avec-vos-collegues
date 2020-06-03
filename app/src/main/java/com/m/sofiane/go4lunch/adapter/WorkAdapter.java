package com.m.sofiane.go4lunch.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
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
import com.m.sofiane.go4lunch.R;
import com.m.sofiane.go4lunch.activity.MainActivity;
import com.m.sofiane.go4lunch.activity.SubActivity;
import com.m.sofiane.go4lunch.models.NameOfResto;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

/**
 * created by Sofiane M. 26/04/2020
 */
public class WorkAdapter extends RecyclerView.Adapter<WorkAdapter.ViewHolder> {
    final Context mContext;
    final FragmentManager mFragmentManager;
    private final ArrayList<NameOfResto> listData;

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
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "RestrictedApi"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int i) {
        Resources res = h.itemView.getContext().getResources();
        if (listData.get(i).getId().equals("2")) {
            String d = res.getString(R.string.nochoice);
            h.txtname.setTextColor(0xffbdbdbd);
            h.txtname.setTypeface(h.txtname.getTypeface(), Typeface.BOLD_ITALIC);
            h.txtname.setText(listData.get(i).getUserName() + " " + " " + d);

        } else {
            String t = res.getString(R.string.choice);
            h.txtname.setText(listData.get(i).getUserName() + " " + t + " " + listData.get(i).getNameOfResto());
            clickOnItem(h, listData.get(i).getPlaceID());
        }

        Glide
                .with(getApplicationContext())
                .load(listData.get(i).getUserPhoto())
                .apply(RequestOptions.circleCropTransform())
                .into(h.urlphoto);
    }

    @SuppressLint("RestrictedApi")
    private void clickOnItem(ViewHolder h, String mPlaceID) {

        h.mButton.setOnClickListener(view -> {

            @SuppressLint("RestrictedApi") Intent intent = new Intent(getApplicationContext(), SubActivity.class);
            intent.putExtra("I", mPlaceID);
            getApplicationContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.sub_name_profilgo)
        TextView txtname;
        @BindView(R.id.sub_photo_profilgo)
        ImageView urlphoto;
        @BindView(R.id.buttonClick)
        Button mButton;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
