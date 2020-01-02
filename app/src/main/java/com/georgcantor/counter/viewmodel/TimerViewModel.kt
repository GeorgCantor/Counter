package com.georgcantor.counter.viewmodel

import android.media.MediaActionSound
import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import com.georgcantor.counter.model.Day
import com.georgcantor.counter.model.DaysDao
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class TimerViewModel(private val dao: DaysDao) : BaseViewModel() {

    companion object {
        private const val START = "Start"
        private const val TIMER = "60:00"
        private const val HOUR = 3600000L
    }

    private lateinit var countDownTimer: CountDownTimer
    var hour = 0
    var formattedTime = MutableLiveData<String>().apply { postValue(TIMER) }
    val timer = MutableLiveData<Long>().apply { postValue(HOUR) }
    val hours = MutableLiveData<String>()
    val buttonText = MutableLiveData<String>().apply { postValue(START) }
    val isStarted = MutableLiveData<Boolean>().apply { postValue(false) }

    val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

    init {
        ioScope.launch {
            val days = dao.getById(currentDate)
            hour = if (days.isNotEmpty()) days.first().hours else 0
            hours.postValue(hour.toString())
        }
    }

    private fun start(length: Long) {
        countDownTimer = object : CountDownTimer(length, 1000) {

            override fun onFinish() {
                hour++
                hours.value = hour.toString()
                formattedTime.value = TIMER
                timer.value = HOUR
                buttonText.value = START
                isStarted.value = false
                MediaActionSound().play(MediaActionSound.START_VIDEO_RECORDING)

                ioScope.launch {
                    if (dao.getById(currentDate).isNotEmpty()) {
                        dao.updateById(currentDate, hour)
                    } else {
                        dao.insert(Day(id = currentDate, hours = hour))
                    }
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                val millis = TimeUnit.MILLISECONDS
                val minutes = millis.toMinutes(millisUntilFinished)
                val seconds = millis.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(millis.toMinutes(millisUntilFinished))

                formattedTime.value = "$minutes:$seconds"
                timer.value = millisUntilFinished
                buttonText.value = "Pause"
                isStarted.value = true
            }

        }.start()
    }

    private fun pause() {
        countDownTimer.cancel()
        buttonText.value = "Continue"
        isStarted.value = false
    }

    fun startOrPause() {
        if (isStarted.value == true) pause() else start(timer.value ?: 0L)
    }

}