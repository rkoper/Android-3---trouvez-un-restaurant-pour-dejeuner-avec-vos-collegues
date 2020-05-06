package com.m.sofiane.go4lunch.fragment;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.BinderThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.work.WorkManager;

import com.m.sofiane.go4lunch.R;
import com.m.sofiane.go4lunch.services.notificationService;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by Sofiane M. 23/04/2020
 */

public class SettingsFragment extends Fragment {


    @BindView(R.id.switchNotif)
    Switch mSwitch;

    @BindView(R.id.ClockNotif)
    TimePicker picker;

    @BindView(R.id.buttonTime)
    Button mButtonTime;

    int h;
    int m;
    
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, null);

//        picker.setIs24HourView(true);
        


        ButterKnife.bind(this, view);
          initSwitch();
        initButton();
        return view;
    }

    @SuppressLint("ResourceAsColor")
    private void initButton() {
        picker.setVisibility(View.INVISIBLE);
        mButtonTime.setVisibility(View.INVISIBLE);

        mButtonTime.setOnClickListener(v -> {
            int hour, minute;
            String am_pm;
            if (Build.VERSION.SDK_INT >= 23 ){
                h = picker.getHour();
                m = picker.getMinute();
            }
            else{
                h = picker.getCurrentHour();
                m = picker.getCurrentMinute();
            }
            if(h > 12) {
                am_pm = "PM";
                h = h - 12;
            }
            else
            {
                am_pm="AM";
            }
            String mTime = "Selected Date: "+ h +":"+ m+" "+am_pm;
            Toast.makeText(getContext(), mTime , Toast.LENGTH_SHORT).show();
        });}


    @SuppressLint("ResourceAsColor")
    public void initSwitch() {

        mSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Toast.makeText(getActivity(), "Switch in action", Toast.LENGTH_SHORT).show();
                    setAlarm();
                    picker.setVisibility(View.VISIBLE);
                    mButtonTime.setVisibility(View.VISIBLE);
                }

            } else {
                WorkManager.getInstance().cancelAllWork();
                Toast.makeText(getActivity(), "OFF", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setAlarm() {

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.HOUR_OF_DAY, h);
        cal.set(Calendar.MINUTE,  m);


        Intent notificationIntent = new Intent(getActivity(), notificationService.class);

        PendingIntent broadcast = PendingIntent.getBroadcast(getActivity(), 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, broadcast);
    }
}


