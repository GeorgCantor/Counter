package com.georgcantor.counter.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData

class TimerViewModel : BaseViewModel() {

    private lateinit var countDownTimer: CountDownTimer
    var millisLeft = MutableLiveData<Long>().apply { postValue(20000) }
    var hour = 0
    val timer = MutableLiveData<String>().apply { postValue("20") }
    val hours = MutableLiveData<String>().apply { postValue(hour.toString()) }
    val buttonText = MutableLiveData<String>().apply { postValue("Start") }
    val isStarted = MutableLiveData<Boolean>().apply { postValue(false) }

    fun startCountdownTimer(length: Long) {
        countDownTimer = object : CountDownTimer(length, 1000) {
            override fun onFinish() {
                hour++
                hours.value = hour.toString()
                timer.value = "20"
                buttonText.value = "Start"
                isStarted.value = false
            }

            override fun onTick(millisUntilFinished: Long) {
                millisLeft.value = millisUntilFinished
                timer.value = (millisUntilFinished / 1000).toString()
                buttonText.value = "Pause"
                isStarted.value = true
            }
        }.start()
    }

    fun pause() {
        countDownTimer.cancel()
        buttonText.value = "Continue"
        isStarted.value = false
    }

}