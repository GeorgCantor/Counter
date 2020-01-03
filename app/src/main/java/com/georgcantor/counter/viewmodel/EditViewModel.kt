package com.georgcantor.counter.viewmodel

import androidx.lifecycle.MutableLiveData
import com.georgcantor.counter.model.DaysDao
import kotlinx.coroutines.launch

class EditViewModel(private val dao: DaysDao) : BaseViewModel() {

    val mutableId = MutableLiveData<String>()

    fun updateDayHours(id: String, hours: Int) {
        ioScope.launch {
            dao.updateById(id, hours)
        }
    }

    fun setId(id: String) {
        mutableId.value = id
    }

}