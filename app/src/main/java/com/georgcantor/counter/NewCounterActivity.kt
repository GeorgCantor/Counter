package com.georgcantor.counter

import android.os.Bundle
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import android.widget.Chronometer
import kotlinx.android.synthetic.main.activity_new_counter.*

class NewCounterActivity : AppCompatActivity() {

    private var isRunning = false
    private var timeWhenStopped: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_counter)

        var chronometer = chronoView

        chronometer.onChronometerTickListener = Chronometer.OnChronometerTickListener {
            chronometer = it
        }

        buttonPlay.setOnClickListener {
            isRunning = if (isRunning) {
                timeWhenStopped = chronometer.base - SystemClock.elapsedRealtime()
                chronometer.stop()
                false
            } else {
                chronometer.base = SystemClock.elapsedRealtime() + timeWhenStopped
                chronometer.start()
                true
            }
        }
    }
}
