package com.georgcantor.counter.viewmodel

import com.georgcantor.counter.model.Day
import com.georgcantor.counter.model.DaysDao
import kotlinx.coroutines.launch

class AddViewModel(private val dao: DaysDao) : BaseViewModel() {

    fun addDay(day: Day) {
        ioScope.launch {
            dao.insert(day)
        }
    }

}