package com.georgcantor.counter.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.georgcantor.counter.R
import com.georgcantor.counter.util.PreferenceManager
import com.georgcantor.counter.util.showDialog
import com.georgcantor.counter.viewmodel.TimerViewModel
import kotlinx.android.synthetic.main.fragment_timer.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class TimerFragment : Fragment() {

    private lateinit var viewModel: TimerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = getViewModel { parametersOf(PreferenceManager(requireActivity())) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_timer, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireContext().showDialog(getString(R.string.exit_message), ::exit)
                }
            })

        viewModel.hours.observe(viewLifecycleOwner, Observer {
            hoursTextView.text = it
        })

        viewModel.buttonText.observe(viewLifecycleOwner, Observer {
            startButton.text = it
        })

        viewModel.formattedTime.observe(viewLifecycleOwner, Observer {
            countdownTextView.text = it
        })

        viewModel.shouldStartAgain.observe(viewLifecycleOwner, Observer { mustStart ->
            if (mustStart) viewModel.startOrPause()
        })

        startButton.setOnClickListener {
            viewModel.startOrPause()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_history -> {
                view?.let { Navigation.findNavController(it).navigate(R.id.historyFragment) }
            }
            R.id.action_graph -> {
                view?.let { Navigation.findNavController(it).navigate(R.id.graphFragment) }
            }
            R.id.action_tune -> {
                view?.let { Navigation.findNavController(it).navigate(R.id.settingsFragment) }
            }
        }
        return false
    }

    private fun exit() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        requireActivity().startActivity(intent)
    }

}