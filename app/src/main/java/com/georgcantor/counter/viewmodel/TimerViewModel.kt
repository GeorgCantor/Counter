package com.georgcantor.counter.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import java.util.concurrent.TimeUnit

class TimerViewModel : BaseViewModel() {

    private lateinit var countDownTimer: CountDownTimer
    var formattedTime = MutableLiveData<String>().apply { postValue("00:20") }
    var hour = 0
    val timer = MutableLiveData<Long>().apply { postValue(20000) }
    val hours = MutableLiveData<String>().apply { postValue(hour.toString()) }
    val buttonText = MutableLiveData<String>().apply { postValue("Start") }
    val isStarted = MutableLiveData<Boolean>().apply { postValue(false) }

    fun startCountdownTimer(length: Long) {
        countDownTimer = object : CountDownTimer(length, 1000) {

            override fun onFinish() {
                hour++
                hours.value = hour.toString()
                formattedTime.value = "00:20"
                timer.value = 20000
                buttonText.value = "Start"
                isStarted.value = false
            }

            override fun onTick(millisUntilFinished: Long) {
                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
                formattedTime.value = "$minutes:$seconds"
                timer.value = millisUntilFinished
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