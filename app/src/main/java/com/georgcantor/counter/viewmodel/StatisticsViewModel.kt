package com.georgcantor.counter.viewmodel

import androidx.lifecycle.MutableLiveData
import com.georgcantor.counter.model.DaysDao
import kotlinx.coroutines.launch
import java.util.*

class StatisticsViewModel(private val dao: DaysDao) : BaseViewModel() {

    val statistics = MutableLiveData<List<String>>()

    fun getDaysStats() {
        ioScope.launch {
            val stats = ArrayList<String>()
            dao.getAll().map {
                stats.add(it.hours.toString())
            }
            statistics.postValue(stats)
        }
    }

}