package com.m.sofiane.go4lunch.fragment;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.work.WorkManager;

import com.m.sofiane.go4lunch.R;
import com.m.sofiane.go4lunch.services.notificationService;
import com.m.sofiane.go4lunch.utils.languageHelper;

import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by Sofiane M. 23/04/2020
 */

public class SettingsFragment extends DialogFragment {

    public SettingsFragment() {}


    private int h;
    private int m;

    Context mContext;

    @BindView(R.id.one_layout_close)
    LinearLayout mLayoutCLose;
    @BindView(R.id.one_button_image_close_settings)
    ImageButton mCloseButton;

    @BindView(R.id.two_layout_notif)
    LinearLayout mLayoutNotif;
    @BindView(R.id.two_layout_language)
    LinearLayout mLayoutLanguage;

    @BindView(R.id.three_layout_subtitle)
    LinearLayout mLayoutSwitch;
    @BindView(R.id.three_switchNotif)
    Switch mSwitch;

    @BindView(R.id.fourR_layout_picker_time)
    LinearLayout mLayoutTimePicker;
    @BindView(R.id.fourR_settings_time_picker)
    TimePicker mTimePicker;
    @BindView(R.id.fourR_texttime)
    TextView mTxtPicker;

    @BindView(R.id.fourL_layout_picker_lang)
    LinearLayout mLayoutLangPicker;
    @BindView(R.id.fourL_switchEN)
    Switch mSwitchEN;
    @BindView(R.id.fourL_switchFR)
    Switch mSwitchFR;
    @BindView(R.id.fourL_txtlanguage)
    TextView mTxtLang;


    @BindView(R.id.Five_button_ok)
    ImageButton mButtonTime;
    @BindView(R.id.five_layout_save_button)
    LinearLayout mLayoutButton;
    Resources mTest;
    Boolean mStatNotif;
    SharedPreferences mSharedPreferences;
    public static final String PREFS ="666";
    public static final String TIMETONOTIF= "TimeToNotif";
    public static final String STATNOTIF= "StateNotif";
    public static final String LANG = "language";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, null);
        ButterKnife.bind(this, view);
        initCloseButton();
        initVisibiliy();
         initWindowsTransprent();

        mTest = getActivity().getResources();


        clickonNotif();
        clickonLangauge();
        return view;
    }

    private void clickonLangauge() {
        mLayoutLanguage.setOnClickListener(v ->
                displayLang());
    }

    private void displayLang(){
        mLayoutSwitch.setVisibility(View.VISIBLE);
        mSwitch.setVisibility(View.INVISIBLE);
        mLayoutLangPicker.setVisibility(View.VISIBLE);
     //   checkLangSwitch();
        chooseYourLanguage();
    }




    private void chooseYourLanguage() {
        mSharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(LANG ,Context.MODE_PRIVATE);
        String mLang  = mSharedPreferences.getString(LANG,"en");
        if (mLang.equals("en"))
        {mSwitchEN.setChecked(true);}
        else {mSwitchFR.setChecked(true);}
        mSwitchFR.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                languageHelper.changeLanguage(mTest, "fr");
                mSharedPreferences.edit().putString(LANG, "fr").apply();
                System.out.println("-----------> GO FR");
                mSwitchEN.setChecked(false);
            }
        });

        mSwitchEN.setOnCheckedChangeListener(((buttonView, isChecked) -> {
         if (isChecked) {
             languageHelper.changeLanguage(mTest, "en");
             mSharedPreferences.edit().putString(LANG, "en").apply();
             System.out.println("-----------> GO EN");
             mSwitchFR.setChecked(false);
         }

        }));
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    private void clickonNotif() {
        mLayoutNotif.setOnClickListener(v ->
                displayNotif());

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void displayNotif(){
        mLayoutSwitch.setVisibility(View.VISIBLE);
        mSwitch.setVisibility(View.VISIBLE);
        mLayoutLangPicker.setVisibility(View.INVISIBLE);
        checkStateSwitch();
        initSwitch();


    }


    private void initWindowsTransprent() {
        Window window = Objects.requireNonNull(getDialog()).getWindow();
        Objects.requireNonNull(window).setBackgroundDrawableResource(android.R.color.transparent);

    }

    private void initVisibiliy() {
        mLayoutButton.setVisibility(View.INVISIBLE);
        mLayoutTimePicker.setVisibility(View.INVISIBLE);
        mLayoutLangPicker.setVisibility(View.INVISIBLE);
        mLayoutSwitch.setVisibility(View.INVISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void checkStateSwitch() {
        mSharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(PREFS ,Context.MODE_PRIVATE);
        Boolean mStatNotif  = mSharedPreferences.getBoolean(STATNOTIF,false);
        if (mStatNotif.equals(true))
        {mSwitch.setChecked(true);
            mLayoutSwitch.setVisibility(View.VISIBLE);
          mLayoutTimePicker.setVisibility(View.VISIBLE);
              mTimePicker.setVisibility(View.VISIBLE);
        initPicker();}
        Log.e("Shared P State ------>", mStatNotif.toString());

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
                else { mLayoutTimePicker.setVisibility(View.VISIBLE);
                    mTimePicker.setVisibility(View.VISIBLE);}

            } else {
                WorkManager.getInstance().cancelAllWork();
                Toast.makeText(getActivity(), "OFF", Toast.LENGTH_SHORT).show();
                mSharedPreferences
                        .edit()
                        .putBoolean(STATNOTIF, false)
                        .remove(TIMETONOTIF)
                        .apply();
                mLayoutTimePicker.setVisibility(View.INVISIBLE);
                mTimePicker.setVisibility(View.INVISIBLE);

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
        });
    }

    private void initButton(Calendar calendar) {
         mButtonTime.setVisibility(View.VISIBLE);
        mLayoutButton.setVisibility(View.VISIBLE);

        mButtonTime.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setAlarm(calendar);
            }
            onDestroyView();
        });
    }

    private void initCloseButton() {
        mCloseButton.setOnClickListener(v -> onDestroyView());
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
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

        // TEST
        boolean mStatNotif  = mSharedPreferences.getBoolean(STATNOTIF,false);
        long mTimeNotif = mSharedPreferences.getLong(TIMETONOTIF, 666);
        Log.e("1 Sha P Click ------>", Boolean.toString(mStatNotif));
        Log.e("1 Sha P Time ------>", Long.toString(mTimeNotif));
    }

}


