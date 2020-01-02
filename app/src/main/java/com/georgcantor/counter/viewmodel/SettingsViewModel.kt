package com.georgcantor.counter.viewmodel

import androidx.lifecycle.ViewModel
import com.georgcantor.counter.util.PreferenceManager
import com.georgcantor.counter.util.PreferenceManager.Companion.FORMATTED_MINUTES
import com.georgcantor.counter.util.PreferenceManager.Companion.MINUTES

class SettingsViewModel(private val manager: PreferenceManager) : ViewModel() {

    fun setMinutes(minutes: String) {
        manager.saveLong(MINUTES, minutes.toLong() * 60 * 1000)
        manager.saveString(FORMATTED_MINUTES, "$minutes:00")
    }

}