package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.Webtoon
import com.example.myapplication.activities.BaseActivity
import com.example.myapplication.adapters.OnItemClickListener
import com.example.myapplication.adapters.WebtoonsListAdapter
import com.example.myapplication.adapters.WebtoonsRecyclerViewHolder

class LibraryFragment : Fragment(), OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_library, container, false)

        // Set up the RecyclerView with a grid layout to display folders in 2 columns
        viewManager = LinearLayoutManager(context)
        viewAdapter = WebtoonsListAdapter(getMyData(), this, R.layout.item_library_webtoon)
        recyclerView = view.findViewById<RecyclerView>(R.id.fragmentLibrary_itemsList).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        return view
    }

    // Give folders name to view
    private fun getMyData(): Array<Any> {
        return arrayOf(
            Webtoon("Webtoon 1", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget ultricies ultrices, nunc nisl aliquam nunc, quis aliquet nisl nunc eu nisl. Donec euismod, nisl eget ultricies ultrices, nunc nisl aliquam nunc, quis aliquet nisl nunc eu nisl."),
            Webtoon("Webtoon 2", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget ultricies ultrices, nunc nisl aliquam nunc, quis aliquet nisl nunc eu nisl. Donec euismod, nisl eget ultricies ultrices, nunc nisl aliquam nunc, quis aliquet nisl nunc eu nisl."),
            Webtoon("Webtoon 3", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget ultricies ultrices, nunc nisl aliquam nunc, quis aliquet nisl nunc eu nisl. Donec euismod, nisl eget ultricies ultrices, nunc nisl aliquam nunc, quis aliquet nisl nunc eu nisl."),
        )
    }

    // Change page when click on a folder
    override fun onItemClick(position: Int, item: Any) {
        println("Clicked on $position")

        val mainActivity = (activity as? BaseActivity)
        val webtoon = item as Webtoon

        mainActivity?.changeTitle(webtoon.getTitle())
        mainActivity?.changeFragment(WebtoonDetailsFragment(webtoon))
    }

    override fun onItemDraw(holder: WebtoonsRecyclerViewHolder, position: Int, item: Any) {
        val webtoon = item as Webtoon
        holder.view.findViewById<TextView>(R.id.itemLibrary_title).text = webtoon.getTitle()
    }
}