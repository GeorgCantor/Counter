package com.georgcantor.counter.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.georgcantor.counter.R
import com.georgcantor.counter.model.Day
import com.georgcantor.counter.view.adapter.HistoryAdapter
import com.georgcantor.counter.viewmodel.HistoryViewModel
import kotlinx.android.synthetic.main.fragment_history.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class HistoryFragment : Fragment() {

    private lateinit var viewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel { parametersOf() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_history, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        daysRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        daysRecyclerView.setHasFixedSize(true)

        viewModel.getDaysStats()
        viewModel.statistics.observe(viewLifecycleOwner, Observer {
            daysRecyclerView.adapter = HistoryAdapter(it as MutableList<Day>)
        })
    }

}