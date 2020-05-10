package com.m.sofiane.go4lunch.services;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.m.sofiane.go4lunch.R;
import com.m.sofiane.go4lunch.activity.mainactivity;
import com.m.sofiane.go4lunch.models.MyChoice;
import com.m.sofiane.go4lunch.utils.mychoiceHelper;

import java.util.List;

import static com.firebase.ui.auth.AuthUI.TAG;

/**
 * created by Sofiane M. 26/04/2020
 */
public class notificationService extends BroadcastReceiver {

    Context mContext;
    String mNameForNotif;


    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        readDataFromFirebase(intent);
    }


    private final void createNotification(Intent intent, String mNameForNotif) {
        Intent intent1 = new Intent(mContext, mainactivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent1, PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle("Go 4 Lunch");
        inboxStyle.addLine("You are expected" + " @ " + mNameForNotif);

        String channelId = "MyID";


        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(mContext, channelId)
                        .setSmallIcon(R.drawable.ic_local_dining_white_24dp)
                        .setContentTitle("Go 4 Lunch")
                        .setContentText("It's time to Lunch")
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent)
                        .setStyle(inboxStyle);

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Message provenant de Firebase";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);

        }
        notificationManager.notify("TAG", 120, notificationBuilder.build());

    }


    public void readDataFromFirebase(Intent intent){
        mychoiceHelper.readMyChoice().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    MyChoice l = document.toObject(MyChoice.class);

                    mNameForNotif = l.getNameOfResto();

                    createNotification(intent, mNameForNotif) ;
                }}});

    }
}