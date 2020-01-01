package com.georgcantor.counter.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import java.util.concurrent.TimeUnit

class TimerViewModel : BaseViewModel() {

    companion object {
        private const val START = "Start"
        private const val TIMER = "60:00"
        private const val HOUR = 3600000L
    }

    private lateinit var countDownTimer: CountDownTimer
    var formattedTime = MutableLiveData<String>().apply { postValue(TIMER) }
    var hour = 0
    val timer = MutableLiveData<Long>().apply { postValue(HOUR) }
    val hours = MutableLiveData<String>().apply { postValue(hour.toString()) }
    val buttonText = MutableLiveData<String>().apply { postValue(START) }
    val isStarted = MutableLiveData<Boolean>().apply { postValue(false) }

    fun startCountdownTimer(length: Long) {
        countDownTimer = object : CountDownTimer(length, 1000) {

            override fun onFinish() {
                hour++
                hours.value = hour.toString()
                formattedTime.value = TIMER
                timer.value = HOUR
                buttonText.value = START
                isStarted.value = false
            }

            override fun onTick(millisUntilFinished: Long) {
                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
                -TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))

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