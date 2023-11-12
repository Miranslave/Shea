package com.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.WebtoonFolder

/**
 * This class is used to display a list of webtoons folders in a RecyclerView.
 */
class WebtoonsFoldersListAdapter(
    private val webtoonsFoldersList: Array<Any>, private val listener: OnItemClickListener, private val itemsLayoutId: Int
) : RecyclerView.Adapter<WebtoonsRecyclerViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): WebtoonsRecyclerViewHolder {
        return WebtoonsRecyclerViewHolder(LayoutInflater.from(parent.context).inflate(itemsLayoutId, parent, false), listener, webtoonsFoldersList)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: WebtoonsRecyclerViewHolder, position: Int) {
        listener.onItemDraw(holder, position, webtoonsFoldersList[position])
        holder.itemView.setOnClickListener { listener.onItemClick(position, webtoonsFoldersList[position]) }
    }

    // Return the size of the dataset (invoked by the layout manager)
    override fun getItemCount() = webtoonsFoldersList.size
}