package com.johnpetitto.orachat.data.user

import android.annotation.SuppressLint
import android.content.Context
import android.preference.PreferenceManager

class UserPreferences(context: Context) {
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    var authorizationToken: String?
        get() = preferences.getString("token", null)
        set(token) = storeToken(token)

    @SuppressLint("ApplySharedPref")
    private fun storeToken(token: String?) {
        preferences.edit().putString("token", token).commit()
    }
}
