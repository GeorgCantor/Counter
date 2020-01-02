package com.georgcantor.counter.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(activity: Activity) {

    companion object {
        const val MY_PREFS = "me_prefs"
        const val MINUTES = "minutes"
    }

    private val prefs: SharedPreferences = activity.getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE)

    fun saveLong(key: String, value: Long) = prefs.edit().putLong(key, value).apply()

    fun getLong(key: String): Long = prefs.getLong(key, 3600000L)

}