package com.example.myapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.Webtoon
import com.example.myapplication.activities.BaseActivity
import com.example.myapplication.adapters.RecyclerViewEventsManager
import com.example.myapplication.adapters.WebtoonsListAdapter
import com.example.myapplication.adapters.WebtoonsRecyclerViewHolder
import com.example.myapplication.viewModels.LibraryViewModel
import com.example.myapplication.viewModels.ViewModelCallback

// This class represents a fragment in the application that displays a list of webtoons in a RecyclerView.
class LibraryFragment : FragmentRecyclerViewManager(), RecyclerViewEventsManager {
    // Initialize the ViewModel
    private var viewModel = LibraryViewModel()

    // This method inflates the layout for this fragment and initializes the RecyclerView with a grid layout to display folders in 2 columns.
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var view = inflater.inflate(R.layout.fragment_library, container, false)
        this.initRecyclerViewDisplay(view, R.id.fragmentLibrary_itemsList, WebtoonsListAdapter(arrayOf<Webtoon>(), this, R.layout.item_library_webtoon), LinearLayoutManager(context))
        return view
    }

    // This method is called after the view is created. It fetches the list of webtoons from the `LibraryViewModel` and sets the content of the RecyclerView.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getWebtoonsList(object : ViewModelCallback<Array<Webtoon>> {
            // On successful fetch, update the RecyclerView with the fetched data.
            override fun onSuccess(result: Array<Webtoon>) {
                Log.d("Success", result.toString())
                setRecyclerViewContent(WebtoonsListAdapter(result, this@LibraryFragment, R.layout.item_library_webtoon))
            }

            // On error, log the error and show a toast message.
            override fun onError(e: Throwable) {
                Log.d("Error", e.toString())
                Toast.makeText(context, "Une erreur empêche l'affichage", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // This method is called when an item in the RecyclerView is clicked. It changes the title of the main activity and replaces the current fragment with a `WebtoonDetailsFragment` that shows the details of the clicked webtoon.
    override fun onItemClick(position: Int, item: Any?) {
        Log.d("info", "Clicked on $position")
        val mainActivity = (activity as? BaseActivity)
        val webtoon = item as Webtoon
        mainActivity?.changeTitle(webtoon.getTitle())
        mainActivity?.changeFragment(WebtoonDetailsFragment(webtoon))
    }

    // This method is called when an item in the RecyclerView is drawn. It sets the title and synopsis of the webtoon on the corresponding TextViews in the item view.
    override fun onItemDraw(holder: WebtoonsRecyclerViewHolder, position: Int, item: Any?) {
        val webtoon = item as Webtoon
        holder.view.findViewById<TextView>(R.id.itemLibrary_title).text = webtoon.getTitle()
        holder.view.findViewById<TextView>(R.id.itemLibrary_synopsis).text = webtoon.getSynopsis()
        // This line of code is commented out. It seems to be intended for setting an image resource to an ImageView, but it's not completed.
        // holder.view.findViewById<ImageView>(R.id.itemLibrary_image).setImageResource()
    }
}
