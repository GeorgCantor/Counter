package com.georgcantor.counter.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.georgcantor.counter.model.Day
import com.georgcantor.counter.model.DaysDao
import kotlinx.coroutines.launch

class HistoryViewModel(private val dao: DaysDao) : ViewModel() {

    val statistics = MutableLiveData<List<Day>>()

    fun getDaysStats() {
        viewModelScope.launch {
            statistics.postValue(dao.getAll())
        }
    }

    fun removeDay(id: String) {
        viewModelScope.launch {
            dao.deleteById(id)
        }
    }

}