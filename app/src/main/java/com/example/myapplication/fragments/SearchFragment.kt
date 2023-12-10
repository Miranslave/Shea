package com.example.myapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.image.ImageLoader
import com.example.myapplication.R
import com.example.myapplication.models.Webtoon
import com.example.myapplication.adapters.RecyclerViewEventsManager
import com.example.myapplication.adapters.WebtoonsListAdapter
import com.example.myapplication.adapters.WebtoonsRecyclerViewHolder
import com.example.myapplication.viewModels.SearchViewModel
import com.example.myapplication.viewModels.ViewModelCallback

class SearchFragment : FragmentRecyclerViewManager(), RecyclerViewEventsManager {
    private val model = SearchViewModel()
    private lateinit var imageLoader: ImageLoader
    private lateinit var spinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        // Initialize the ImageLoader
        this.imageLoader = ImageLoader("https://webtoon-phinf.pstatic.net", requireContext())

        // Initialize the RecyclerView
        this.initRecyclerViewDisplay(view, R.id.fragmentSearch_itemsList, WebtoonsListAdapter(listOf<Webtoon>(), this, R.layout.item_library_webtoon), LinearLayoutManager(context))

        // Start the loading animation
        this.spinner = Spinner(this, false)

        // Get the SearchView and bind a listener to it
        val searchView = view.findViewById<SearchView>(R.id.fragmentSearch_searchView)
        searchView.setOnQueryTextListener(searchQueryListener(this.model))

        // Call the API to get the list of Webtoons to initially show()
        this.model.getWebtoonList(getWebtoonListCallback())

        return view
    }

    private fun getWebtoonListCallback(): ViewModelCallback<List<Webtoon>> {
        return object : ViewModelCallback<List<Webtoon>> {
            override fun onSuccess(result: List<Webtoon>) {
                setRecyclerViewContent(WebtoonsListAdapter(result, this@SearchFragment, R.layout.item_library_webtoon))
                spinner.stop()
            }

            override fun onError(e: Throwable) {
                Toast.makeText(context, getString(R.string.display_error), Toast.LENGTH_SHORT).show()
                spinner.stop()
            }
        }
    }

    private fun searchQueryListener(model: SearchViewModel): SearchView.OnQueryTextListener {
        return object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                spinner.start()
                model.searchForWebtoon(newText.toString(), object : ViewModelCallback<List<Webtoon>> {
                    override fun onSuccess(result: List<Webtoon>) {
                        setRecyclerViewContent(WebtoonsListAdapter(result, this@SearchFragment, R.layout.item_library_webtoon))
                        spinner.stop()
                    }

                    override fun onError(e: Throwable) {
                        Toast.makeText(context, getString(R.string.display_error), Toast.LENGTH_SHORT).show()
                        spinner.stop()
                    }
                })
                return true
            }
        }
    }

    override fun onItemClick(position: Int, item: Any?) {
        Log.d("Click", "Click on item $position")
    }

    override fun onItemDraw(holder: WebtoonsRecyclerViewHolder, position: Int, item: Any?) {
        val webtoon = item as Webtoon
        holder.view.findViewById<TextView>(R.id.itemLibrary_title).text = webtoon.getTitle()
        holder.view.findViewById<TextView>(R.id.itemLibrary_synopsis).text = webtoon.getSynopsis()
        this.imageLoader.load(holder.view.findViewById(R.id.itemLibrary_image), webtoon.getThumbnail())
    }
}