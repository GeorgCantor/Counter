package com.georgcantor.counter.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.georgcantor.counter.R
import com.georgcantor.counter.model.Day
import kotlinx.android.synthetic.main.day_item.view.*

class HistoryAdapter(days: MutableList<Day>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private val days: MutableList<Day> = ArrayList()

    init {
        this.days.addAll(days)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder =
        HistoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.day_item, null))

    override fun getItemCount(): Int = days.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.dateTextView.text = days[position].id
        holder.hoursTextView.text = days[position].hours.toString()
    }

    class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateTextView: TextView = view.dateTextView
        val hoursTextView: TextView = view.hoursTextView
    }

}