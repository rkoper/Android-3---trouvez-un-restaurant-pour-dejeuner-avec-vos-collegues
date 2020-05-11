package com.m.sofiane.go4lunch.fragment;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
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

public class SettingsFragment extends DialogFragment {

    public SettingsFragment() {}

    @BindView(R.id.switchNotif)
    Switch mSwitch;
    @BindView(R.id.settings_time_picker)
    TimePicker mTimePicker;
    @BindView(R.id.buttonTime)
    ImageButton mButtonTime;
    private int h;
    private int m;
    @BindView(R.id.layoutbutton)
    LinearLayout mLayoutButton;
    @BindView(R.id.layouttimepicker)
    LinearLayout mLayoutTimePicker;
    @BindView(R.id.layoutswitch)
    LinearLayout mLayoutSwitch;
    @BindView(R.id.layoutclose)
    LinearLayout mLayoutCLose;
    @BindView(R.id.button_image_close_settings)
    ImageButton mCloseButton;
    @BindView(R.id.layoutTitle)
    LinearLayout mLayoutTitle;
    @BindView(R.id.txt_title_settings)
    TextView mTitleTxt;



    final Fragment mapFragment = new MapFragment();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, null);
        ButterKnife.bind(this, view);
        initSwitch();

        initCloseButton();
        mLayoutButton.setVisibility(View.INVISIBLE);
        mLayoutTimePicker.setVisibility(View.INVISIBLE);

        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);

        return view;
    }



    @SuppressLint("ResourceAsColor")
    public void initSwitch() {

        mSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mLayoutTimePicker.setVisibility(View.VISIBLE);
                    mTimePicker.setVisibility(View.VISIBLE);
                    initPicker();
                }

            } else {
                WorkManager.getInstance().cancelAllWork();
                Toast.makeText(getActivity(), "OFF", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ResourceAsColor")
    private void initPicker() {
        mTimePicker.is24HourView();
        mTimePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            Calendar cal = Calendar.getInstance();

            cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
            cal.set(Calendar.MINUTE, minute);
           initButton(cal);
            Log.e("Picker 1 ------->" , cal.toString());
        });
    }

    private void initButton(Calendar calendar) {
         mButtonTime.setVisibility(View.VISIBLE);
        mLayoutButton.setVisibility(View.VISIBLE);

        mButtonTime.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                setAlarm(calendar);
                Log.e("Picker 2 ------->" , calendar.toString());
                onDestroyView();
            }
        });



    }

    private void initCloseButton() {
        mCloseButton.setOnClickListener(v -> {
            onDestroyView();
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setAlarm( Calendar cal) {
        Intent notificationIntent = new Intent(getActivity(), notificationService.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(getActivity(), 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Log.e("Picker 3 ------->" , cal.toString());
        Toast.makeText(getActivity(), cal.toString(), Toast.LENGTH_SHORT).show();
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, broadcast);
    }

}


