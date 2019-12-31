package com.georgcantor.counter.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.georgcantor.counter.R
import com.georgcantor.counter.viewmodel.TimerViewModel
import kotlinx.android.synthetic.main.fragment_timer.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class TimerFragment : Fragment() {

    private lateinit var viewModel: TimerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel { parametersOf() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_timer, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var isStarted = false
        viewModel.isStarted.observe(viewLifecycleOwner, Observer {
            isStarted = it
        })
        viewModel.timer.observe(viewLifecycleOwner, Observer {
            countdownTextView.text = it
        })

        startButton.setOnClickListener {
            if (isStarted) {
                viewModel.pause()
            } else {
                val last = (countdownTextView.text.toString().toLong()) * 1000
                viewModel.startCountdownTimer(last)
                viewModel.hours.observe(viewLifecycleOwner, Observer {
                    hoursTextView.text = it
                })
            }
        }
        viewModel.buttonText.observe(viewLifecycleOwner, Observer {
            startButton.text = it
        })
    }

}