package com.georgcantor.counter.viewmodel

import androidx.lifecycle.ViewModel
import com.georgcantor.counter.util.PreferenceManager
import com.georgcantor.counter.util.PreferenceManager.Companion.MINUTES

class SettingsViewModel(private val manager: PreferenceManager) : ViewModel() {

    fun setMinutes(minutes: Long) {
        manager.saveLong(MINUTES, minutes)
    }

}