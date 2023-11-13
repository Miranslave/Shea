package com.example.myapplication.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView


// Provide a reference to the views for each data item
class WebtoonsRecyclerViewHolder(val view: View, private val listener: RecyclerViewEventsManager, private val items:Array<*>) : RecyclerView.ViewHolder(view) {
    init {
        view.setOnClickListener {
            listener.onItemClick(adapterPosition, items[adapterPosition])
        }
    }
}