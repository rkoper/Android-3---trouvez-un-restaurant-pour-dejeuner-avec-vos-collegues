package com.m.sofiane.go4lunch.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.work.WorkManager;

import com.m.sofiane.go4lunch.R;
import com.m.sofiane.go4lunch.activity.mainactivity;
import com.m.sofiane.go4lunch.services.notificationService;

import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.*;

/**
 * created by Sofiane M. 23/04/2020
 */

public class SettingsFragment extends DialogFragment {

    @BindView(R.id.check_notif)
    CheckBox mCheckNotif;
    @BindView(R.id.check_lang)
    CheckBox mCheckLang;
    @BindView(R.id.lfrench)
    LinearLayout mLfrench;
    @BindView(R.id.lnotif)
    LinearLayout mLTimePicker;
    @BindView(R.id.switch_notif)
    Switch mSwNotif;
    @BindView(R.id.widget_time_picker)
    TimePicker mWidgetTimePicker;
    @BindView(R.id.button_ok)
    ImageButton mButtonOK;
    @BindView(R.id.button_close)
    LinearLayout mButton_Close;
    @BindView(R.id.switch_lang_fr)
    Switch mSwitchFrench;
    @BindView(R.id.switch_lang_en)
    Switch mSwitchEnglish;

    Boolean mStatNotif;
    Animation mASD, mASU, mAup, mASD2, mASU2, mASD3, mASU3;
    SharedPreferences mSharedPreferences;

    public static final String PREFS = "666";
    public static final String TIMETONOTIF = "TimeToNotif";
    public static final String STATNOTIF = "StateNotif";
    public static final String LANG = "language";

    public SettingsFragment() { }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_test, null);
        initAnimation();
        ButterKnife.bind(this, view);
        initWindowsTransprent();
        initLang();
        initNotif();
        initCloseButton();
        return view;
    }

    private void initAnimation() {
        mASD = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);
        mASU = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
        mASD2 = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down2);
        mASU2 = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up2);
        mASD3 = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down3);
        mASU3 = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up3);
        mAup = AnimationUtils.loadAnimation(getContext(), R.anim.inbottom);
    }

    private void initCloseButton() {
        mButton_Close.setOnClickListener(v ->
                onDestroyView());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    private void initLang() {
        mCheckLang.setOnClickListener(v -> {
            if (mCheckLang.isChecked()) {
                mLfrench.startAnimation(mASU);
                chooseYourLanguage(); }
            else {
                mLfrench.startAnimation(mASD);}
        });
    }

    private void chooseYourLanguage() {
        mSharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(LANG, Context.MODE_PRIVATE);
        String mLang = mSharedPreferences.getString(LANG, "en");
        if (mLang.equals("en")) {
            mSwitchEnglish.setChecked(true); }
        else {
            mSwitchFrench.setChecked(true); }

        mSwitchFrench.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mSharedPreferences.edit().putString(LANG, "fr").apply();
                mSwitchEnglish.setChecked(false);
                Intent refresh = new Intent(getContext(), mainactivity.class);
                startActivity(refresh);
            }
        });
        mSwitchEnglish.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if (isChecked) {
                mSharedPreferences.edit().putString(LANG, "en").apply();
                mSwitchFrench.setChecked(false);
                Intent refresh = new Intent(getContext(), mainactivity.class);
                startActivity(refresh); }
        }));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initNotif() {
        mCheckNotif.setOnClickListener(v -> {
            if (mCheckNotif.isChecked()) {
                mLTimePicker.startAnimation(mASU2);
                LoadNotifSwitch(); }
            else  { mLTimePicker.startAnimation(mASD2); }
        });
    }

    private void LoadNotifSwitch() {
        mSharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        mStatNotif = mSharedPreferences.getBoolean(STATNOTIF, false);
        if (mStatNotif.equals(true)) {
            mSwNotif.setChecked(true);
            mWidgetTimePicker.setVisibility(VISIBLE);
            initPicker();
        } else {mSwNotif.setChecked(false);}
        mSwNotif.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (mSwNotif.isChecked()) {
                initPicker();
                mWidgetTimePicker.setVisibility(VISIBLE);
                mCheckNotif.setEnabled(false); }
            else {
                WorkManager.getInstance().cancelAllWork();
                mSharedPreferences
                        .edit()
                        .putBoolean(STATNOTIF, false)
                        .apply();
                mWidgetTimePicker.setVisibility(INVISIBLE);
                mButtonOK.setVisibility(GONE);
                mCheckNotif.setEnabled(true); }});

        Log.e("Shared P State ------>", mStatNotif.toString());
    }

    private void initPicker() {
        mWidgetTimePicker.is24HourView();
        mWidgetTimePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
            cal.set(Calendar.MINUTE, minute);
            mButtonOK.setVisibility(VISIBLE);
            mButtonOK.startAnimation(mAup);
            initButtonOK(cal);
        });
    }

    private void initButtonOK(Calendar cal) {
        mButtonOK.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setAlarm(cal);
            }
            onDestroyView();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setAlarm( Calendar cal) {
        Intent notificationIntent = new Intent(getActivity(), notificationService.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(getActivity(), 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) Objects.requireNonNull(getActivity()).getSystemService(Context.ALARM_SERVICE);

        Objects.requireNonNull(alarmManager).setRepeating(AlarmManager.RTC_WAKEUP,
                cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, broadcast);

        mSharedPreferences
                .edit()
                .putBoolean(STATNOTIF, true)
                .putLong(TIMETONOTIF, (cal.getTimeInMillis()))
                .apply();
    }

    private void initWindowsTransprent() {
        Window window = Objects.requireNonNull(getDialog()).getWindow();
        Objects.requireNonNull(window).setBackgroundDrawableResource(android.R.color.transparent);
    }

}