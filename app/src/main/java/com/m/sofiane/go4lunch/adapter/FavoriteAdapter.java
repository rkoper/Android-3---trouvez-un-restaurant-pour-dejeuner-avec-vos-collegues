package com.m.sofiane.go4lunch.adapter;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.m.sofiane.go4lunch.R;
import com.m.sofiane.go4lunch.models.MyFavorite;
import com.m.sofiane.go4lunch.utils.myfavoriteHelper;

import java.util.List;
import java.util.Objects;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


/**
 * created by Sofiane M. 2020-01-30
 */
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private final List<MyFavorite> listData;
    final Context mContext;
    final FragmentManager mFragmentManager;
    ImageButton mButtonFav;
    String name;

    public FavoriteAdapter(List<MyFavorite> listData, FragmentManager mFragmentManager, Context mContext) {
        this.listData = listData;
        this.mFragmentManager = mFragmentManager;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_fav, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int i) {
        MyFavorite ld = listData.get(i);
        callName(h, ld);
        callPhoto(h, ld);
        callAdress(h, ld);
        deleteItem( ld, i);
    }


    public void deleteItem(MyFavorite ld, int i ) {
        mButtonFav.setOnClickListener(v -> {
            if (listData == null) {
            } else {
                String itemLabel = listData.get(i).getName();
                listData.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, listData.size());
                Toast.makeText((v.getContext()), "Removed : " + itemLabel, Toast.LENGTH_SHORT).show();
                String t = ld.getName();
                Log.e(t, t);

                myfavoriteHelper.deleteMyFav(t)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                    Log.d("FAv DATA 1", document.getId() + " => " + document.getData());
                                    document.getReference().delete();
                                }
                            } else {
                                Log.d("FAV DATA 2", "Error getting documents: ", task.getException());
                            }
                        });
            }

        });
    }

    @SuppressLint("RestrictedApi")
    private void callPhoto(ViewHolder h, MyFavorite ld) {
        String UrlPhoto = ld.getPhoto();

        Glide.
                with(getApplicationContext())
                .load(UrlPhoto)
                .apply(RequestOptions.circleCropTransform())
                .into(h.urlphoto);
    }

    private void callAdress(ViewHolder h, MyFavorite ld) {
        h.txtadress.setText(ld.getAdress());
    }

    private void callName(ViewHolder h, MyFavorite ld) {
        name = ld.getName();
        h.txtname.setText(name);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtname;
        private final TextView txtadress;
        final ImageView urlphoto;


        public ViewHolder(View itemView) {
            super(itemView);
            txtname = itemView.findViewById(R.id.place_namefav);
            txtadress = itemView.findViewById(R.id.place_addressfav);
            urlphoto = itemView.findViewById(R.id.place_photofav);
            mButtonFav = itemView.findViewById(R.id.imageButton);
        }
    }
}