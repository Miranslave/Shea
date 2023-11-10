package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        viewManager = GridLayoutManager(context, 2)
        viewAdapter = WebtoonFoldersListAdapter(getMyData())

        recyclerView = view.findViewById<RecyclerView>(R.id.homeItemsList).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        return view
    }

    // Give folders name to view
    private fun getMyData(): Array<String> {
        return arrayOf(
            "Action",
            "Comédie",
            "Drame",
            "Fantastique",
            "Horreur",
            "Romance",
            "Science-fiction",
            "Tranche de vie"
        )
    }
}