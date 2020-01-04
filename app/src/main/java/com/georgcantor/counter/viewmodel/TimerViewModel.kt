package com.georgcantor.counter.viewmodel

import android.media.MediaActionSound
import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.georgcantor.counter.model.Day
import com.georgcantor.counter.model.DaysDao
import com.georgcantor.counter.util.PreferenceManager
import com.georgcantor.counter.util.PreferenceManager.Companion.FORMATTED_MINUTES
import com.georgcantor.counter.util.PreferenceManager.Companion.MINUTES
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class TimerViewModel(
    private val manager: PreferenceManager,
    private val dao: DaysDao
) : ViewModel() {

    companion object {
        private const val START = "Старт"
    }

    private val minutes: Long by lazy { manager.getLong(MINUTES) }
    private val formatted: String by lazy { manager.getString(FORMATTED_MINUTES) ?: "60:00" }

    private lateinit var countDownTimer: CountDownTimer
    var hour = 0.0F
    var formattedTime = MutableLiveData<String>().apply { postValue(formatted) }
    val timer = MutableLiveData<Long>().apply { postValue(minutes) }
    val hours = MutableLiveData<String>()
    val buttonText = MutableLiveData<String>().apply { postValue(START) }
    val isStarted = MutableLiveData<Boolean>().apply { postValue(false) }
    val shouldStartAgain = MutableLiveData<Boolean>().apply { postValue(false) }

    val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

    init {
        viewModelScope.launch {
            val days = dao.getById(currentDate)
            hour = if (days.isNotEmpty()) days.first().hours else 0.0F
            hours.postValue(hour.toString())
        }
    }

    fun startOrPause() {
        if (isStarted.value == true) pause() else start(timer.value ?: 0L)
    }

    private fun start(length: Long) {
        countDownTimer = object : CountDownTimer(length, 1000) {

            override fun onFinish() {
                hour++
                hours.value = hour.toString()
                formattedTime.value = formatted
                timer.value = minutes
                buttonText.value = START
                isStarted.value = false
                shouldStartAgain.value = true
                MediaActionSound().play(MediaActionSound.START_VIDEO_RECORDING)

                viewModelScope.launch {
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
                buttonText.value = "Пауза"
                isStarted.value = true
                shouldStartAgain.value = false
            }

        }.start()
    }

    private fun pause() {
        countDownTimer.cancel()
        buttonText.value = "Продолжить"
        isStarted.value = false
    }

}