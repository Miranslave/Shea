package com.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * This class is used to display a list of webtoons in a RecyclerView.
 */
class WebtoonsListAdapter(
    private val webtoonsList: List<*>, private val listener: RecyclerViewEventsManager, private val itemsLayoutId: Int

) : RecyclerView.Adapter<WebtoonsRecyclerViewHolder>() {


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): WebtoonsRecyclerViewHolder {
        return WebtoonsRecyclerViewHolder(LayoutInflater.from(parent.context).inflate(itemsLayoutId, parent, false), listener, webtoonsList)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: WebtoonsRecyclerViewHolder, position: Int) {
        listener.onItemDraw(holder, position, webtoonsList[position])
        holder.itemView.setOnClickListener { listener.onItemClick(position, webtoonsList[position]) }
    }

    // Return the size of the dataset (invoked by the layout manager)
    override fun getItemCount() = webtoonsList.size

    fun getWebtoonsList(): List<*> {
        return this.webtoonsList
    }
}