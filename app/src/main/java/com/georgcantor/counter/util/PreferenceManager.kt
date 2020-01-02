package com.georgcantor.counter.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(activity: Activity) {

    companion object {
        const val MY_PREFS = "my_prefs"
        const val MINUTES = "minutes"
        const val FORMATTED_MINUTES = "formatted_minutes"
    }

    private val prefs: SharedPreferences = activity.getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE)

    fun saveLong(key: String, value: Long) = prefs.edit().putLong(key, value).apply()

    fun getLong(key: String): Long = prefs.getLong(key, 3600000L)

    fun saveString(key: String, value: String) = prefs.edit().putString(key, value).apply()

    fun getString(key: String): String? = prefs.getString(key, "60:00")

}