package com.example.coreutills.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SharedPreference {

    static final String PREFS_NAME = "preferences";

    private static SharedPreference shared = new SharedPreference();

    private SharedPreference() {
    }

    public static SharedPreference getInstance() {
        return shared;
    }

    public void clearSharedPreferences(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        settings.edit().clear().apply();
    }

    public void removeKey(String key, Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        settings.edit().remove(key).apply();
    }


    public void SaveSharedPreference(Context context, final String key, final String value) {
        try {
            SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            Editor editor = settings.edit();

            editor.putString(key, value);

            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String GetSharedPreference(Context context, String key, String defaultValue) {
        try {
            SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            Log.d("deb", "Inside" + settings.getString(key,defaultValue));
            return settings.getString(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public void SaveSharedPreference(Context context, final String key, final boolean value) {
        try {
            SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            Editor editor = settings.edit();

            editor.putBoolean(key, value);

            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean GetSharedPreference(Context context, String key, boolean defaultValue) {
        try {
            SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            return settings.getBoolean(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void SaveSharedPreference(Context context, final String key, final Integer value) {
        try {
            SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            Editor editor = settings.edit();

            editor.putInt(key, value);

            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Integer GetSharedPreferenceInt(Context context, String key, Integer defaultValue) {
        try {
            SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            return settings.getInt(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return defaultValue;
    }

    public void SaveSharedPreferenceLong(Context context, final String key, final long value) {
        try {
            SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            Editor editor = settings.edit();

            editor.putLong(key, value);

            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Long GetSharedPreferenceLong(Context context, String key, long defaultValue) {
        try {
            SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            return settings.getLong(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return defaultValue;
    }
}

