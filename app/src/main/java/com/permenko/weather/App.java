package com.permenko.weather;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class App extends Application {

    private static final String INITIAL_STATE = "INITIAL_STATE";
    private static SharedPreferences defaultSharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration config = new RealmConfiguration.Builder(this)
                //.deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        //init defaultSharedPreferences
        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    //i'm not sure that it's the best way, but
    //lets store app default preferences here
    public static boolean isInitialState() {
        return defaultSharedPreferences.getBoolean(INITIAL_STATE, true);
    }

    public static void markStateNotInitial() {
        defaultSharedPreferences.edit().putBoolean(INITIAL_STATE, false).apply();
    }

}
