package com.georgcantor.counter.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.georgcantor.counter.model.DaysDao
import kotlinx.coroutines.launch

class EditViewModel(private val dao: DaysDao) : ViewModel() {

    val mutableId = MutableLiveData<String>()

    fun updateDayHours(id: String, hours: Float) {
        viewModelScope.launch {
            dao.updateById(id, hours)
        }
    }

    fun setId(id: String) {
        mutableId.value = id
    }

}