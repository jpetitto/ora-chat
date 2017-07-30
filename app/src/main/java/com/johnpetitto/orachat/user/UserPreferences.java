package com.johnpetitto.orachat.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class UserPreferences {
    private SharedPreferences preferences;

    public UserPreferences(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getAuthorizationToken() {
        return preferences.getString("token", null);
    }

    @SuppressLint("ApplySharedPref")
    public void setAuthorizationToken(String token) {
        // synchronized with commit for background thread write
        preferences.edit().putString("token", token).commit();
    }
}
