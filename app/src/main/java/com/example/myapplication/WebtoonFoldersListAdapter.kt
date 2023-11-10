package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * This class is used to display a list of webtoons in a RecyclerView.
 */
class WebtoonFoldersListAdapter(
    private val dataset: Array<String>, private val listener: OnItemClickListener
) : RecyclerView.Adapter<WebtoonFoldersViewHolder>() {


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): WebtoonFoldersViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_webtoon_folder, parent, false)
        return WebtoonFoldersViewHolder(view, listener)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: WebtoonFoldersViewHolder, position: Int) {
        holder.view.findViewById<TextView>(R.id.itemWebtoon_folderName).text = dataset[position]
        holder.itemView.setOnClickListener { listener.onItemClick(position) }
    }

    // Return the size of the dataset (invoked by the layout manager)
    override fun getItemCount() = dataset.size
}

// Provide a reference to the views for each data item
class WebtoonFoldersViewHolder(val view: View, private val listener: OnItemClickListener) : RecyclerView.ViewHolder(view) {
    init {
        view.setOnClickListener {
            listener.onItemClick(adapterPosition)
        }
    }
}

interface OnItemClickListener {
    fun onItemClick(position: Int)
}