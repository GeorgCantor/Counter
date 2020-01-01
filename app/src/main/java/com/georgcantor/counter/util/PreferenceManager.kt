package com.georgcantor.counter.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(activity: Activity) {

    companion object {
        const val MY_PREFS = "my_prefs"
    }

    private val prefs: SharedPreferences = activity.getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE)

    fun saveBoolean(key: String, value: Boolean) = prefs.edit().putBoolean(key, value).apply()

    fun saveString(key: String, value: String) = prefs.edit().putString(key, value).apply()

    fun saveInt(key: String, value: Int) = prefs.edit().putInt(key, value).apply()

    fun saveLong(key: String, value: Long) = prefs.edit().putLong(key, value).apply()

    fun getBoolean(key: String): Boolean = prefs.getBoolean(key, false)

    fun getString(key: String): String? = prefs.getString(key, "")

    fun getInt(key: String): Int = prefs.getInt(key, 0)

    fun getLong(key: String): Long = prefs.getLong(key, 0L)

}