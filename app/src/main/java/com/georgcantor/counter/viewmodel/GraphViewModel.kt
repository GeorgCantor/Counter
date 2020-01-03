package com.georgcantor.counter.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.georgcantor.counter.model.DaysDao
import kotlinx.coroutines.launch

class GraphViewModel(private val dao: DaysDao) : ViewModel() {

    val daysStats = MutableLiveData<List<Float>>()

    fun getData() {
        viewModelScope.launch {
            val hours = dao.getAll().map {
                it.hours
            }
            daysStats.postValue(hours)
        }
    }

}