package com.georgcantor.counter.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.georgcantor.counter.R
import com.georgcantor.counter.viewmodel.GraphViewModel
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.fragment_graph.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class GraphFragment : Fragment() {

    private lateinit var viewModel: GraphViewModel
    private lateinit var series: LineGraphSeries<DataPoint>
    private var lastX = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel { parametersOf() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_graph, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        series = LineGraphSeries()
        graph.addSeries(series)

        val viewport = graph.viewport
        viewport.isYAxisBoundsManual = true
        viewport.setMinY(0.0)
        viewport.setMaxY(10.0)
        viewport.isScrollable = true

        buildGraph()
    }

    private fun buildGraph() {
        viewModel.getData()
        viewModel.daysStats.observe(viewLifecycleOwner, Observer { stats ->
            stats.map {
                series.appendData(DataPoint(lastX++, it.toDouble()), true, 365)
            }
        })
    }

}