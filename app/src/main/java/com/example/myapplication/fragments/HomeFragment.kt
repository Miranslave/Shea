package com.example.myapplication.fragments

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.R
import com.example.myapplication.WebtoonFolder
import com.example.myapplication.activities.BaseActivity
import com.example.myapplication.adapters.RecyclerViewEventsManager
import com.example.myapplication.adapters.WebtoonsFoldersListAdapter
import com.example.myapplication.adapters.WebtoonsRecyclerViewHolder
import com.example.myapplication.firestoredb.data.Firestore
import com.example.myapplication.firestoredb.data.FirestoreCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HomeFragment : FragmentRecyclerViewManager(), RecyclerViewEventsManager {
    // Folders deletion mode attributes
    private var deleteFolderMode: Boolean = false
    private var deleteFolderList: MutableList<String> = mutableListOf()

    // Other attributes
    private val db: FirebaseFirestore = Firebase.firestore
    private lateinit var spinner: Spinner
    private lateinit var floatingDeleteButton: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Set up the RecyclerView with a grid layout to display folders in 2 columns
        this.initRecyclerViewDisplay(
            view, R.id.fragmentHome_itemsList, WebtoonsFoldersListAdapter(listOf<WebtoonFolder>(), this, R.layout.item_webtoon_folder), GridLayoutManager(context, 2)
        )

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val floatingCreationButton: FloatingActionButton = view.findViewById(R.id.fragmentHome_newFolderButton)
        this.floatingDeleteButton = view.findViewById(R.id.fragmentHome_deleteFolderButton)

        Log.d("Connected user id", uid)
        showDatabaseFolders(uid)

        // Folder creation popup
        floatingCreationButton.setOnClickListener(fun(_: View) {
            val builder = AlertDialog.Builder(this.context)
            builder.setTitle(getString(R.string.create_folder))

            // Set up the input field
            val titleinput = EditText(this.context)
            titleinput.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(titleinput)

            // Creation button
            builder.setPositiveButton(getString(R.string.create)) { _, _ ->
                val folderTitle = titleinput.text.toString()
                addFolderToDatabase(uid, folderTitle)
                showDatabaseFolders(uid)
            }

            // Cancel button
            builder.setNegativeButton(getString(R.string.abort)) { dialog, _ -> dialog.cancel() }

            // Show the AlertDialog
            builder.show()

            // Set the focus on the input field
            titleinput.requestFocus()
        })

        // Folder deletion popup
        this.floatingDeleteButton.setOnClickListener(fun(_: View) {
            deleteFolderFromDatabase(this.deleteFolderList)
            showDatabaseFolders(uid)
            switchDeleteMode(false)
        })
    }

    private fun removeElementFromArray(array: Array<String>, toRemove: String): Array<String> {
        val res = emptyArray<String>()
        for (str in array) {
            if (str != toRemove) res.plusElement(str)
        }
        return res
    }

    private fun addFolderToDatabase(uid: String, title: String) {
        val webtoonFolder = hashMapOf(
            "uid" to uid, "title" to title, "description" to "a link  au +", "webtoonsid" to arrayListOf<Int>()
        )

        db.collection("WebtoonFolder").document().set(webtoonFolder).addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }.addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    private fun deleteFolderFromDatabase(databaseIdsList: List<String>) {
        for (id in databaseIdsList) db.collection("WebtoonFolder").document(id).delete()
    }

    // Update the RecyclerView with the fetched data
    private fun showDatabaseFolders(uid: String) {
        setRecyclerViewContent(WebtoonsFoldersListAdapter(listOf<WebtoonFolder>(), this, R.layout.item_webtoon_folder))

        this.spinner = Spinner(this)
        Firestore().WebtoonFolder(uid, object : FirestoreCallback<List<WebtoonFolder>> {
            override fun onSuccess(result: List<Any>) {
                Log.d("Success", result.toString())
                spinner.stop()

                // If there is no folder, display a toaster
                if (result.isEmpty()) {
                    Toast.makeText(context, getString(R.string.no_folder), Toast.LENGTH_SHORT).show()
                } else {
                    setRecyclerViewContent(
                        WebtoonsFoldersListAdapter(
                            result, this@HomeFragment, R.layout.item_webtoon_folder
                        )
                    )
                }

            }

            override fun onError(e: Throwable) {
                Log.d("Error", e.toString())
                spinner.stop()
                Toast.makeText(context, getString(R.string.display_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Change page when click on a folder
    override fun onItemClick(position: Int, item: Any?) {
        val mainActivity = (activity as? BaseActivity)
        val webtoonFolder = item as WebtoonFolder

        // The folder needs to be unselected
        if (deleteFolderMode) {
            // Change folder color to white and unselect it
            val holder = getRecyclerView().findViewHolderForAdapterPosition(position) as WebtoonsRecyclerViewHolder
            holder.view.setBackgroundResource(R.color.white)
            deleteFolderList.remove(webtoonFolder.getdbid())

            // Hide the delete button if there is no more folder to delete
            if (deleteFolderList.isEmpty())
                switchDeleteMode(false)
        } else {
            mainActivity?.changeFragment(WebtoonFolderDetailsFragment(webtoonFolder))
            mainActivity?.changeTitle(webtoonFolder.getTitle())
        }
    }

    // Set each item folder title to the view
    override fun onItemDraw(holder: WebtoonsRecyclerViewHolder, position: Int, item: Any?) {
        val webtoonFolder = item as WebtoonFolder
        holder.view.findViewById<TextView>(R.id.itemFolder_title).text = webtoonFolder.getTitle()

        holder.itemView.setOnLongClickListener {
            // Change folder color to light blue
            holder.view.setBackgroundResource(R.color.main_light_blue)

            // Start the deletion mode
            switchDeleteMode(true)

            // Add the folder to the list of folders to delete
            deleteFolderList.add(webtoonFolder.getdbid())

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
}