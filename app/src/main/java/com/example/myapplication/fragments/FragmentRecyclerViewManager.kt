package com.example.myapplication.fragments

import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapters.WebtoonsListAdapter

// Class to manage RecyclerView in a Fragment
open class FragmentRecyclerViewManager : Fragment() {
    // Declare RecyclerView, Adapter and LayoutManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    // Function to initialize RecyclerView display
    fun initRecyclerViewDisplay(view: View, recyclerViewId: Int, adapt: RecyclerView.Adapter<*>, layout: RecyclerView.LayoutManager) {
        // Assign passed parameters to class variables
        this.viewManager = layout
        this.viewAdapter = adapt

        // Initialize RecyclerView with passed parameters
        this.recyclerView = view.findViewById<RecyclerView>(recyclerViewId).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    // Function to set content for RecyclerView
    fun setRecyclerViewContent(adapter: RecyclerView.Adapter<*>) {
        // Assign passed adapter to class variable and set it to RecyclerView
        this.viewAdapter = adapter
        this.recyclerView.adapter = this.viewAdapter
    }

    fun changeRecyclerViewLayout(layout: RecyclerView.LayoutManager) {
        this.viewManager = layout
        this.recyclerView.layoutManager = this.viewManager
    }

    fun getRecyclerView(): RecyclerView {
        return this.recyclerView
    }

    fun getRecyclerViewContentList(): List<*> {
        return (this.recyclerView.adapter as WebtoonsListAdapter).getWebtoonsList()
    }
}
