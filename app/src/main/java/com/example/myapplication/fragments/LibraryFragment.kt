package com.example.myapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.activities.BaseActivity
import com.example.myapplication.adapters.RecyclerViewEventsManager
import com.example.myapplication.adapters.WebtoonsListAdapter
import com.example.myapplication.adapters.WebtoonsRecyclerViewHolder
import com.example.myapplication.image.ImageLoader
import com.example.myapplication.models.Webtoon
import com.example.myapplication.viewModels.LibraryViewModel
import com.example.myapplication.viewModels.ViewModelCallback

// This class represents a fragment in the application that displays a list of webtoons in a RecyclerView.
class LibraryFragment : FragmentRecyclerViewManager(), RecyclerViewEventsManager {
    private var listDisplayLayout: Int = R.layout.item_library_webtoon_list
    private lateinit var spinner: Spinner

    // Initialize the ViewModel
    private var viewModel = LibraryViewModel()
    private lateinit var imageLoader: ImageLoader

    // This method inflates the layout for this fragment and initializes the RecyclerView with a grid layout to display folders in 2 columns.
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        this.imageLoader = ImageLoader("https://webtoon-phinf.pstatic.net", requireContext())

        val view = inflater.inflate(R.layout.fragment_library, container, false)
        this.initRecyclerViewDisplay(view, R.id.fragmentLibrary_itemsList, WebtoonsListAdapter(listOf<Webtoon>(), this, this.listDisplayLayout), LinearLayoutManager(context))
        return view
    }

    // This method is called after the view is created. It fetches the list of webtoons from the `LibraryViewModel` and sets the content of the RecyclerView.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Animate the spinner
        spinner = Spinner(this.requireView().findViewById(R.id.fragmentLibrary_loading))

        // Fetch the list of webtoons from the ViewModel
        this.viewModel.getWebtoonsList(object : ViewModelCallback<List<Webtoon>> {
            // On successful fetch, update the RecyclerView with the fetched data.
            override fun onSuccess(result: List<Webtoon>) {
                if (result.isEmpty()) {
                    view.findViewById<ImageView>(R.id.fragmentLibrary_loading).setImageResource(R.drawable.star_filled)
                } else {
                    setRecyclerViewContent(WebtoonsListAdapter(result, this@LibraryFragment, listDisplayLayout))
                    spinner.stop()
                }
            }

            // On error, log the error and show a toast message.
            override fun onError(e: Throwable) {
                Log.d("Error", e.toString())
                Toast.makeText(context, getString(R.string.display_error), Toast.LENGTH_SHORT).show()
                spinner.stop()
            }
        })

        // Listener for the display mode button (grid or list)
        val listDisplayModeButton = view.findViewById<ImageButton>(R.id.fragmentLibrary_listDisplayButton)
        listDisplayModeButton.setOnClickListener {
            this.listDisplayLayout = R.layout.item_library_webtoon_list
            this.setRecyclerViewContent(WebtoonsListAdapter(this.getRecyclerViewContentList(), this@LibraryFragment, R.layout.item_library_webtoon_list))
            this.changeRecyclerViewLayout(LinearLayoutManager(context))
        }
        val gridDisplayModeButton = view.findViewById<ImageButton>(R.id.fragmentLibrary_gridDisplayButton)
        gridDisplayModeButton.setOnClickListener {
            this.listDisplayLayout = R.layout.item_library_webtoon_grid
            this.setRecyclerViewContent(WebtoonsListAdapter(this.getRecyclerViewContentList(), this@LibraryFragment, R.layout.item_library_webtoon_grid))
            this.changeRecyclerViewLayout(GridLayoutManager(context, 2))
        }

        // Listener for the search bar
        val searchBar = view.findViewById<SearchView>(R.id.fragmentLibrary_searchBar)
        searchBar.setOnQueryTextListener(searchQueryListener())
    }

    // This method returns a listener for the search bar. It fetches the list of webtoons from the `LibraryViewModel` and sets the content of the RecyclerView.
    private fun searchQueryListener(): SearchView.OnQueryTextListener {
        return object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchForWebtoon(newText.toString(), object : ViewModelCallback<List<Webtoon>> {
                    override fun onSuccess(result: List<Webtoon>) {
                        setRecyclerViewContent(WebtoonsListAdapter(result, this@LibraryFragment, listDisplayLayout))
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

    // This method is called when an item in the RecyclerView is clicked. It changes the title of the main activity and replaces the current fragment with a `WebtoonDetailsFragment` that shows the details of the clicked webtoon.
    override fun onItemClick(position: Int, item: Any?) {
        val mainActivity = (activity as? BaseActivity)
        val webtoon = item as Webtoon

        mainActivity?.changeTitle(webtoon.getTitle())
        mainActivity?.changeFragment(WebtoonDetailsFragment(webtoon))
    }

    // This method is called when an item in the RecyclerView is drawn. It sets the title and synopsis of the webtoon on the corresponding TextViews in the item view.
    override fun onItemDraw(holder: WebtoonsRecyclerViewHolder, position: Int, item: Any?) {
        val webtoon = item as Webtoon
        holder.view.findViewById<TextView>(R.id.itemLibrary_title).text = webtoon.getTitle()
        holder.view.findViewById<TextView>(R.id.itemLibrary_synopsis)?.text = webtoon.getSynopsis()
        this.imageLoader.load(holder.view.findViewById(R.id.itemLibrary_image), webtoon.getThumbnail())
    }
}