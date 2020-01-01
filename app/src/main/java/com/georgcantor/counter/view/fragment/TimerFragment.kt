package com.georgcantor.counter.view.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.georgcantor.counter.R
import com.georgcantor.counter.viewmodel.TimerViewModel
import kotlinx.android.synthetic.main.fragment_timer.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class TimerFragment : Fragment() {

    private lateinit var viewModel: TimerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = getViewModel { parametersOf() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_timer, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.hours.observe(viewLifecycleOwner, Observer {
            hoursTextView.text = it
        })

        startButton.setOnClickListener {
            viewModel.startOrPause()
        }
        viewModel.buttonText.observe(viewLifecycleOwner, Observer {
            startButton.text = it
        })

        viewModel.formattedTime.observe(viewLifecycleOwner, Observer {
            countdownTextView.text = it
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_history -> {
                view?.let { Navigation.findNavController(it).navigate(R.id.statisticsFragment) }
            }
        }
        return false
    }

}