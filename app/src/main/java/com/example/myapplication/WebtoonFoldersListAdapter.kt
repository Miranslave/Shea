package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * This class is used to display a list of webtoons in a RecyclerView.
 */
class WebtoonFoldersListAdapter(private val dataset: Array<String>) : RecyclerView.Adapter<WebtoonFoldersListAdapter.MyViewHolder>() {

    // Provide a reference to the views for each data item
    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): MyViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_webtoon_folder, parent, false)
        return MyViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.view.findViewById<TextView>(R.id.folder_name).text = dataset[position]
    }

    // Return the size of the dataset (invoked by the layout manager)
    override fun getItemCount() = dataset.size
}