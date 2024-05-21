package io.paysky.paybutton.example;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class SettingPrefs {


    public static boolean getBooleanPrefs(Context context, String key, boolean defaultValue) {
        SharedPreferences defaultSharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context);
        return defaultSharedPreferences.getBoolean(key, defaultValue);
    }

    public static void saveBooleanPrefs(Context context, String key, boolean value) {
        SharedPreferences defaultSharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context);
        defaultSharedPreferences.edit().putBoolean(key, value).apply();
    }

    public static String getStringPrefs(Context context, String key, String defaultValue) {
        SharedPreferences defaultSharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context);
        return defaultSharedPreferences.getString(key, defaultValue);
    }

    public static void saveStringPrefs(Context context, String key, String value) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        defaultSharedPreferences.edit().putString(key, value).apply();
    }
}
