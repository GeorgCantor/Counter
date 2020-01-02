package com.georgcantor.counter.viewmodel

import androidx.lifecycle.MutableLiveData
import com.georgcantor.counter.model.DaysDao
import kotlinx.coroutines.launch

class GraphViewModel(private val dao: DaysDao) : BaseViewModel() {

    val daysStats = MutableLiveData<List<Int>>()

    fun getData() {
        ioScope.launch {
            val hours = dao.getAll().map {
                it.hours
            }
            daysStats.postValue(hours)
        }
    }

}