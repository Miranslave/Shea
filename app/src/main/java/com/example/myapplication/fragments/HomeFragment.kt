package com.example.myapplication.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.activities.BaseActivity
import com.example.myapplication.adapters.RecyclerViewEventsManager
import com.example.myapplication.adapters.WebtoonsFoldersListAdapter
import com.example.myapplication.adapters.WebtoonsListAdapter
import com.example.myapplication.adapters.WebtoonsRecyclerViewHolder
import com.example.myapplication.models.WebtoonFolder
import com.example.myapplication.viewModels.HomeViewModel
import com.example.myapplication.viewModels.ViewModelCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton


class HomeFragment : FragmentRecyclerViewManager(), RecyclerViewEventsManager {
    // Folders deletion mode attributes
    private var deleteFolderMode: Boolean = false
    private var personalFoldersToDeleteList: MutableList<WebtoonFolder> = mutableListOf()
    private var sharedFoldersToDeleteList: MutableList<WebtoonFolder> = mutableListOf()

    // Other attributes
    private val viewModel: HomeViewModel = HomeViewModel()
    private lateinit var spinner: Spinner
    private lateinit var floatingDeleteButton: FloatingActionButton
    private lateinit var view: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_home, container, false)

        // Set up the RecyclerView with a grid layout to display folders in 2 columns
        this.initRecyclerViewDisplay(
            view, R.id.fragmentHome_personalItemsList, WebtoonsFoldersListAdapter(listOf<WebtoonFolder>(), this, R.layout.item_webtoon_folder), GridLayoutManager(context, 2)
        )

        return this.view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val floatingCreationButton: FloatingActionButton = view.findViewById(R.id.fragmentHome_newFolderButton)
        val searchBar = view.findViewById<SearchView>(R.id.fragmentHome_searchBar)
        this.floatingDeleteButton = view.findViewById(R.id.fragmentHome_deleteFolderButton)

        showDatabasePersonalFolders()
        showDatabaseSharedFolders()
        showDatabaseFollowedFolders()

        // Folder creation popup
        floatingCreationButton.setOnClickListener(fun(_: View) {
            val builder = AlertDialog.Builder(this.context)
            builder.setTitle(getString(R.string.create_folder))

            // Set up the input field
            val titleInput = EditText(this.context)
            titleInput.inputType = InputType.TYPE_CLASS_TEXT
            titleInput.hint = getString(R.string.title)
            builder.setView(titleInput)

            val descInput = EditText(this.context)
            descInput.inputType = InputType.TYPE_CLASS_TEXT
            descInput.hint = getString(R.string.description)
            builder.setView(descInput)

            val radioGroup = RadioGroup(this.context)
            radioGroup.orientation = RadioGroup.HORIZONTAL
            radioGroup.gravity = Gravity.CENTER_HORIZONTAL

            val radioButton1 = RadioButton(this.context)
            radioButton1.text = getString(R.string.shared)
            radioGroup.addView(radioButton1)
            radioGroup.check(radioButton1.id)

            val radioButton2 = RadioButton(this.context)
            radioButton2.text = getString(R.string.unshared)
            radioGroup.addView(radioButton2)

            radioButton1.setOnClickListener() {
                radioButton2.isChecked = false
                radioButton1.isChecked = true
            }
            radioButton2.setOnClickListener() {
                radioButton1.isChecked = false
                radioButton2.isChecked = true
            }

            val layout = LinearLayout(this.context)
            layout.orientation = LinearLayout.VERTICAL
            layout.gravity = Gravity.CENTER
            layout.addView(titleInput)
            layout.addView(descInput)
            layout.addView(radioGroup)
            builder.setView(layout)

            // Creation button
            builder.setPositiveButton(getString(R.string.create)) { _, _ ->
                val folderTitle = titleInput.text.toString()
                val folderDesc = descInput.text.toString()
                val selectedOption = when (radioGroup.checkedRadioButtonId) {
                    radioButton1.id -> "public"
                    radioButton2.id -> "private"
                    else            -> "Ca n'arrive pas normalement..."
                }

                if (folderTitle.isEmpty()) {
                    Toast.makeText(context, getString(R.string.no_title), Toast.LENGTH_SHORT).show()
                } else {
                    this.viewModel.addFolderToDatabase(folderTitle, folderDesc, selectedOption)
                    showDatabasePersonalFolders()
                }
            }

            // Cancel button
            builder.setNegativeButton(getString(R.string.abort)) { dialog, _ -> dialog.cancel() }

            // Show the AlertDialog
            builder.show()

            // Set the focus on the input field
            titleInput.requestFocus()
        })

        // Folder deletion popup
        this.floatingDeleteButton.setOnClickListener(fun(_: View) {
            // Ask for deletion
            val builder = AlertDialog.Builder(this.context)
            builder.setTitle(getString(R.string.delete_folder))
            builder.setMessage(getString(R.string.delete_folder_confirmation))

            // Deletion button
            builder.setPositiveButton(getString(R.string.delete)) { _, _ ->
                this.viewModel.deleteFolderFromDatabase(this.personalFoldersToDeleteList + this.sharedFoldersToDeleteList)
                showDatabasePersonalFolders()
                switchDeleteMode(false)
            }

            // Cancel button
            builder.setNegativeButton(getString(R.string.abort)) { dialog, _ ->
                switchDeleteMode(false)
                dialog.cancel()
            }

            // Show the AlertDialog
            builder.show()
        })

        // Listener for the search bar
        searchBar.setOnQueryTextListener(searchQueryListener())
    }


    // Update the RecyclerView with the fetched data
    private fun showDatabasePersonalFolders() {
        // Show an empty list first to reset the previous datas
        setRecyclerViewContent(WebtoonsFoldersListAdapter(listOf<WebtoonFolder>(), this, R.layout.item_webtoon_folder))

        this.spinner = Spinner(this.requireView().findViewById(R.id.fragmentHome_loading))

        viewModel.getPersonalWebtoonFoldersList(object : ViewModelCallback<List<WebtoonFolder>> {
            override fun onSuccess(result: List<WebtoonFolder>) {
                // If there is no folder, display a toaster
                if (result.isEmpty()) {
                    Toast.makeText(context, getString(R.string.no_folder), Toast.LENGTH_SHORT).show()
                } else {
                    setRecyclerViewContent(WebtoonsFoldersListAdapter(result, this@HomeFragment, R.layout.item_webtoon_folder))
                }
                spinner.stop()
            }

            override fun onError(e: Throwable) {
                Log.d("Webtoon folders fetch error", e.toString())
                Toast.makeText(context, getString(R.string.display_error), Toast.LENGTH_SHORT).show()
                spinner.stop()
            }
        })
    }

    private fun showDatabaseSharedFolders() {
        // Show an empty list first to reset the previous datas
        val publicViewManager = GridLayoutManager(context, 2)
        val publicViewAdapter = WebtoonsFoldersListAdapter(listOf<WebtoonFolder>(), this, R.layout.item_webtoon_folder)
        val publicRecyclerView = view.findViewById<RecyclerView>(R.id.fragmentHome_sharedItemsList)
        publicRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = publicViewManager
            adapter = publicViewAdapter
        }

        viewModel.getWebtoonSharedFolderList(object : ViewModelCallback<List<WebtoonFolder>> {
            override fun onSuccess(result: List<WebtoonFolder>) {
                publicRecyclerView.adapter = WebtoonsFoldersListAdapter(result, this@HomeFragment, R.layout.item_webtoon_folder)
            }

            override fun onError(e: Throwable) {
                Log.d("Webtoon public folders fetch error", e.toString())
                Toast.makeText(context, getString(R.string.display_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showDatabaseFollowedFolders() {
        val followedViewManager = GridLayoutManager(context, 2)
        val followedViewAdapter = WebtoonsFoldersListAdapter(listOf<WebtoonFolder>(), this, R.layout.item_webtoon_folder)
        val followedRecyclerView = view.findViewById<RecyclerView>(R.id.fragmentHome_followedItemsList)
        followedRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = followedViewManager
            adapter = followedViewAdapter
        }

        viewModel.getWebtoonFollowedFolderList(object : ViewModelCallback<List<WebtoonFolder>> {
            override fun onSuccess(result: List<WebtoonFolder>) {
                followedRecyclerView.adapter = WebtoonsFoldersListAdapter(result, this@HomeFragment, R.layout.item_webtoon_folder)
            }

            override fun onError(e: Throwable) {
                Log.d("Webtoon public folders fetch error", e.toString())
                Toast.makeText(context, getString(R.string.display_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun searchQueryListener(): SearchView.OnQueryTextListener {
        return object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchForWebtoonFolder(newText.toString(), object : ViewModelCallback<List<WebtoonFolder>> {
                    override fun onSuccess(result: List<WebtoonFolder>) {
                        setRecyclerViewContent(WebtoonsListAdapter(result, this@HomeFragment, R.layout.item_webtoon_folder))
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

    // Change page when click on a folder
    override fun onItemClick(position: Int, item: Any?) {
        val mainActivity = (activity as? BaseActivity)
        val webtoonFolder = item as WebtoonFolder

        // The folder needs to be selected or unselected but not opened
        if (deleteFolderMode) {
            val selection = webtoonFolder.canBeDeleted() && personalFoldersToDeleteList.find { it.getDatabaseId() == webtoonFolder.getDatabaseId() } == null && sharedFoldersToDeleteList.find { it.getDatabaseId() == webtoonFolder.getDatabaseId() } == null
            val folderIsInSharedRecyclerView = webtoonFolder.isPublic() && webtoonFolder.getAuthorId() != viewModel.connectedUser?.uid.toString()

            // If the folder is already selected, unselect it otherwise select it
            if (folderIsInSharedRecyclerView) {
                setDeleteFolderSelection(R.id.fragmentHome_sharedItemsList, position, webtoonFolder, selection)
            } else {
                setDeleteFolderSelection(R.id.fragmentHome_personalItemsList, position, webtoonFolder, selection)
            }

            // Hide the delete button if there is no more folder to delete
            if (personalFoldersToDeleteList.isEmpty() && sharedFoldersToDeleteList.isEmpty()) {
                switchDeleteMode(false)
            }
        } else {
            mainActivity?.changeFragment(WebtoonFolderDetailsFragment(webtoonFolder))
            mainActivity?.changeTitle(webtoonFolder.getTitle())
        }
    }

    // Set each item folder title to the view
    override fun onItemDraw(holder: WebtoonsRecyclerViewHolder, position: Int, item: Any?) {
        val webtoonFolder = item as WebtoonFolder
        holder.view.findViewById<TextView>(R.id.itemFolder_title).text = webtoonFolder.getTitle()

        if (webtoonFolder.getTitle() == "Favoris") {
            holder.view.findViewById<ImageView>(R.id.itemFolder_image).setImageResource(R.drawable.star_filled)
        } else if (webtoonFolder.isPublic()) {
            holder.view.findViewById<ImageView>(R.id.itemFolder_image).setImageResource(R.drawable.world)
        } else {
            holder.view.findViewById<ImageView>(R.id.itemFolder_image).setImageResource(R.drawable.lock)
        }

        // Long click on items
        holder.itemView.setOnLongClickListener {
            val folderIsInSharedRecyclerView = webtoonFolder.isPublic() && webtoonFolder.getAuthorId() != viewModel.connectedUser?.uid.toString()

            // Select the folder
            if (folderIsInSharedRecyclerView) {
                setDeleteFolderSelection(R.id.fragmentHome_sharedItemsList, position, webtoonFolder, webtoonFolder.canBeDeleted())
            } else {
                setDeleteFolderSelection(R.id.fragmentHome_personalItemsList, position, webtoonFolder, webtoonFolder.canBeDeleted())
            }

            true
        }
    }

    private fun switchDeleteMode(status: Boolean) {
        this.deleteFolderMode = status

        if (status) {
            this.floatingDeleteButton.visibility = View.VISIBLE
            this.floatingDeleteButton.bringToFront()
        } else {
            this.floatingDeleteButton.visibility = View.GONE
        }
    }

    private fun setDeleteFolderSelection(listId: Int, position: Int, webtoonFolder: WebtoonFolder, select: Boolean) {
        val holder: RecyclerView.ViewHolder = if (listId == R.id.fragmentHome_personalItemsList) {
            getRecyclerView().findViewHolderForAdapterPosition(position) as WebtoonsRecyclerViewHolder
        } else {
            view.findViewById<RecyclerView>(R.id.fragmentHome_sharedItemsList).findViewHolderForAdapterPosition(position) as WebtoonsRecyclerViewHolder
        }

        val folderIsInSharedRecyclerView = webtoonFolder.isPublic() && webtoonFolder.getAuthorId() != viewModel.connectedUser?.uid.toString()

        if (select) {
            // Change folder color to light blue and select it
            holder.itemView.setBackgroundResource(R.drawable.border_radius_blue)

            if (folderIsInSharedRecyclerView) {
                sharedFoldersToDeleteList.add(webtoonFolder)
            } else {
                personalFoldersToDeleteList.add(webtoonFolder)
            }
        } else {
            // Change folder color to white and unselect it
            holder.itemView.setBackgroundResource(R.drawable.border_radius_white)

            if (folderIsInSharedRecyclerView) {
                sharedFoldersToDeleteList.remove(webtoonFolder)
            } else {
                personalFoldersToDeleteList.remove(webtoonFolder)
            }
        }

        // Start the deletion mode
        switchDeleteMode(personalFoldersToDeleteList.isNotEmpty() || sharedFoldersToDeleteList.isNotEmpty())
    }
}