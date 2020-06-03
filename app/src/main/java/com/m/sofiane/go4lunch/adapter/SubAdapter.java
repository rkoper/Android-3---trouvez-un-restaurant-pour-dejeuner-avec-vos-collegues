package com.m.sofiane.go4lunch.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.m.sofiane.go4lunch.R;
import com.m.sofiane.go4lunch.activity.MainActivity;
import com.m.sofiane.go4lunch.models.NameOfResto;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

/**
 * created by Sofiane M. 25/04/2020
 */
public class SubAdapter extends RecyclerView.Adapter<SubAdapter.ViewHolder> {
    final ArrayList<NameOfResto> listDataName;
    final ArrayList listDataPhoto;
    final Context mContext;
    final FragmentManager mFragmentManager;

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

        return new ViewHolder(view);
    }

    @SuppressLint({"RestrictedApi", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int i) {
        Resources res = h.itemView.getContext().getResources();
        if (listDataName == null) {
            Toast.makeText(mContext, R.string.error, Toast.LENGTH_SHORT).show();
        } else {
            h.txtname.setText(String.format("%s %s", listDataName.get(i).getUserName(), res.getString((R.string.comingg))));
        }

        Glide
                .with(getApplicationContext())
                .load(Objects.requireNonNull(listDataName).get(i).getUserPhoto())
                .apply(RequestOptions.circleCropTransform())
                .into(h.urlphoto);
    }

    @Override
    public int getItemCount() {
        return listDataName.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.sub_name_profil)
        TextView txtname;
        @BindView(R.id.sub_photo_profil)
        ImageView urlphoto;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}