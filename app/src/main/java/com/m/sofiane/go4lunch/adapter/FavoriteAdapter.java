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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.m.sofiane.go4lunch.models.MyFavorite;
import com.m.sofiane.go4lunch.R;
import com.m.sofiane.go4lunch.utils.myfavoriteHelper;
import com.m.sofiane.go4lunch.utils.myuserhelper;

import java.util.List;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


/**
 * created by Sofiane M. 2020-01-30
 */
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private List<MyFavorite> listData;
    Context mContext;
    FragmentManager mFragmentManager;
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

            String itemLabel = listData.get(i).getName();
            listData.remove(i);
            notifyItemRemoved(i);
            notifyItemRangeChanged(i,listData.size());
            Toast.makeText((v.getContext()),"Removed : " + itemLabel,Toast.LENGTH_SHORT).show();

            String t = ld.getName();
            Log.e( t,t);


            myfavoriteHelper.deleteMyFav(t)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("FAv DATA 1", document.getId() + " => " + document.getData());
                                document.getReference().delete();
                            }
                        } else {
                            Log.d("FAV DATA 2", "Error getting documents: ", task.getException());
                        }
                    });


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
        private TextView txtname, txtadress;
        ImageView urlphoto;
        ImageButton mImageButton;


        public ViewHolder(View itemView) {
            super(itemView);
            txtname = (TextView) itemView.findViewById(R.id.place_namefav);
            txtadress = (TextView) itemView.findViewById(R.id.place_addressfav);
            urlphoto = (ImageView) itemView.findViewById(R.id.place_photofav);
            mButtonFav = (ImageButton) itemView.findViewById(R.id.imageButtonFav);

        }
    }


}