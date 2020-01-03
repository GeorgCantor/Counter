package com.georgcantor.counter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.georgcantor.counter.model.Day
import com.georgcantor.counter.model.DaysDao
import kotlinx.coroutines.launch

class AddViewModel(private val dao: DaysDao) : ViewModel() {

    fun addDay(day: Day) {
        viewModelScope.launch {
            dao.insert(day)
        }
    }

}