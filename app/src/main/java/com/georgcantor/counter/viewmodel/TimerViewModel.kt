package com.georgcantor.counter.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData

class TimerViewModel : BaseViewModel() {

    val timer = MutableLiveData<String>()
    val hours = MutableLiveData<String>()

    fun startCountdownTimer() {
        object : CountDownTimer(10000, 1000) {
            override fun onFinish() {
                hours.value = "1"
            }

            override fun onTick(millisUntilFinished: Long) {
                timer.value = (millisUntilFinished / 1000).toString()
            }
        }.start()
    }

}