package com.georgcantor.counter.viewmodel

import androidx.lifecycle.MutableLiveData
import com.georgcantor.counter.model.Day
import com.georgcantor.counter.model.DaysDao
import kotlinx.coroutines.launch

class HistoryViewModel(private val dao: DaysDao) : BaseViewModel() {

    val statistics = MutableLiveData<List<Day>>()

    fun getDaysStats() {
        ioScope.launch {
            statistics.postValue(dao.getAll())
        }
    }

}