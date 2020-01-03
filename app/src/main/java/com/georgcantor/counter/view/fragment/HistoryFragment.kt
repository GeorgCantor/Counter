package com.georgcantor.counter.view.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.georgcantor.counter.R
import com.georgcantor.counter.model.Day
import com.georgcantor.counter.view.adapter.HistoryAdapter
import com.georgcantor.counter.viewmodel.EditViewModel
import com.georgcantor.counter.viewmodel.HistoryViewModel
import kotlinx.android.synthetic.main.fragment_history.*
import org.koin.androidx.viewmodel.ext.android.getSharedViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class HistoryFragment : Fragment() {

    private lateinit var viewModel: HistoryViewModel
    private lateinit var editViewModel: EditViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = getViewModel { parametersOf() }
        editViewModel = getSharedViewModel { parametersOf() }
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
            daysRecyclerView.adapter = HistoryAdapter(it as MutableList<Day>) { day ->
                editViewModel.setId(day.id)
                Navigation.findNavController(view).navigate(R.id.editFragment)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add -> {
                view?.let { Navigation.findNavController(it).navigate(R.id.addFragment) }
            }
        }
        return false
    }

}