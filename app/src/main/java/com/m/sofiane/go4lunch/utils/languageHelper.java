package com.m.sofiane.go4lunch.utils;

import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class languageHelper {

    public static void changeLanguage(Resources res, String locale) {
        Configuration mConfig;
        mConfig = new Configuration(res.getConfiguration());

        switch (locale){
            case "fr" :
                mConfig.locale= new Locale("fr");
                break;

            case "en" :
                mConfig.locale = new Locale("en");
        }

        res.updateConfiguration(mConfig, res.getDisplayMetrics());

    }
}
