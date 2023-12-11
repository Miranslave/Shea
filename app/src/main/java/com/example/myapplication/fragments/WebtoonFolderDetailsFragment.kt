package com.example.myapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.activities.BaseActivity
import com.example.myapplication.adapters.RecyclerViewEventsManager
import com.example.myapplication.adapters.WebtoonsListAdapter
import com.example.myapplication.adapters.WebtoonsRecyclerViewHolder
import com.example.myapplication.image.ImageLoader
import com.example.myapplication.models.Webtoon
import com.example.myapplication.models.WebtoonFolder
import com.example.myapplication.viewModels.HomeViewModel
import com.example.myapplication.viewModels.LibraryViewModel
import com.example.myapplication.viewModels.ViewModelCallback
import com.google.android.material.textview.MaterialTextView

class WebtoonFolderDetailsFragment(private val folder: WebtoonFolder) : FragmentRecyclerViewManager(),
    RecyclerViewEventsManager, BackButtonHandler {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_webtoon_folder_details, container, false)
        this.initRecyclerViewDisplay(view, R.id.fragmentWebtoonFolderDetails_itemsList, WebtoonsListAdapter(listOf<Webtoon>(), this, R.layout.item_library_webtoon_list), LinearLayoutManager(context))

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Animate the spinner
        val spinner = Spinner(this.requireView().findViewById(R.id.fragmentWebtoonFolderDetails_loading))

        // Set the title
        val title: TextView = view.findViewById(R.id.fragmentWebtoonFolderDetails_titleTextField)
        title.text = folder.getTitle()
        title.isEnabled = false


        // Set the description
        val description: EditText = view.findViewById(R.id.fragmentWebtoonFolderDetails_descriptionText)
        description.setText(folder.getDescription())
        description.isEnabled = false

        // Set the back button to go back to the home page
        view.findViewById<TextView>(R.id.fragmentWebtoonFolderDetails_previousPageButton).setOnClickListener {
            this.goBack()
        }

        // Set the edit button
        view.findViewById<AppCompatImageButton>(R.id.fragmentWebtoonFolderDetails_editButton).setOnClickListener {
            title.isEnabled = !title.isEnabled
            description.isEnabled = !description.isEnabled
            HomeViewModel().changeFolderInfo(title.text.toString(),description.text.toString(),folder.getDatabaseId())
        }

        // Set the add webtoon button
        view.findViewById<MaterialTextView>(R.id.fragmentWebtoonFolderDetails_addWebtoonButton).setOnClickListener {
            (activity as? BaseActivity)?.changeFragment(SearchFragment())
            (activity as? BaseActivity)?.changeTitle(getString(R.string.search_tab_title))
        }

        val webtoonList = folder.getWebtoons()
        val webtoonIdList = mutableListOf<Int>()

        for(webtoon in webtoonList){
            webtoonIdList.add(webtoon.getId())
        }
        val viewModel = LibraryViewModel()
        viewModel.setIdList(webtoonIdList)
        viewModel.getWebtoonsList(object : ViewModelCallback<List<Webtoon>> {
            // On successful fetch, update the RecyclerView with the fetched data.
            override fun onSuccess(result: List<Webtoon>) {
                spinner.stop()
                setRecyclerViewContent(WebtoonsListAdapter(result, this@WebtoonFolderDetailsFragment, R.layout.item_library_webtoon_list))
            }

            // On error, log the error and show a toast message.
            override fun onError(e: Throwable) {
                spinner.stop()
                Log.d("Error", e.toString())
                Toast.makeText(context, getString(R.string.display_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onItemClick(position: Int, item: Any?) {
        val mainActivity = (activity as? BaseActivity)
        val webtoon = item as Webtoon

        mainActivity?.changeTitle(webtoon.getTitle())
        mainActivity?.changeFragment(WebtoonDetailsFragment(webtoon))
        Log.d("Thumbnail", webtoon.getThumbnail())
    }

    override fun onItemDraw(holder: WebtoonsRecyclerViewHolder, position: Int, item: Any?) {
        val webtoon = item as Webtoon
        holder.view.findViewById<TextView>(R.id.itemLibrary_title).text = webtoon.getTitle()
        holder.view.findViewById<TextView>(R.id.itemLibrary_synopsis)?.text = webtoon.getSynopsis()
        val imageLoader = ImageLoader("https://webtoon-phinf.pstatic.net", requireContext())
        imageLoader.load(holder.view.findViewById(R.id.itemLibrary_image), webtoon.getThumbnail())
    }

    override fun goBack() {
        (activity as? BaseActivity)?.changeFragment(HomeFragment())
        (activity as? BaseActivity)?.changeTitle(getString(R.string.home_tab_title))
    }
}