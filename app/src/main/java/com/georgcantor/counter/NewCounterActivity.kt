package com.georgcantor.counter

import android.os.Bundle
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import android.widget.Chronometer
import kotlinx.android.synthetic.main.activity_new_counter.*

class NewCounterActivity : AppCompatActivity() {

    private var isRunning = false
    private var timeWhenStopped: Long = 0
    private var hoursCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_counter)

        var chronometer = chronoView

        chronometer.onChronometerTickListener = Chronometer.OnChronometerTickListener {
            chronometer = it
            var timeElapsed = chronometer.text.toString()
            if (timeElapsed == "00:08") {
                timeWhenStopped = 0
                isRunning = false
                buttonPlay.text = "Play"
                chronometer.stop()

                chronometer.text = "00:00"

                hoursCounter += 1
                textViewResult.text = hoursCounter.toString()
            }
        }

        buttonPlay.setOnClickListener {
            isRunning = if (isRunning) {
                timeWhenStopped = chronometer.base - SystemClock.elapsedRealtime()
                buttonPlay.text = "Play"
                chronometer.stop()
                false
            } else {
                chronometer.base = SystemClock.elapsedRealtime() + timeWhenStopped
                buttonPlay.text = "Pause"
                chronometer.start()
                true
            }
        }
    }
}
