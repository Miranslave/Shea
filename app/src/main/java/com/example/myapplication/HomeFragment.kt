package com.example.myapplication

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment(), OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Set up the RecyclerView with a grid layout to display folders in 2 columns
        viewManager = GridLayoutManager(context, 2)
        viewAdapter = WebtoonFoldersListAdapter(getMyData(), this)
        recyclerView = view.findViewById<RecyclerView>(R.id.fragmentHome_itemsList).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val floatingCreationButton: FloatingActionButton = view.findViewById(R.id.fragmentHome_newFolderButton)

        floatingCreationButton.setOnClickListener(fun(_: View) {
            // Create an AlertDialog builder
            val builder = AlertDialog.Builder(this.context)
            builder.setTitle("Quel est le nom du dossier ?")

            // Set up the input field
            val input = EditText(this.context)
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(input)

            // Set up the buttons
            builder.setPositiveButton("Créer") { _, _ ->
                val folderTitle = input.text.toString()
                // Handle the folder title here
            }

            builder.setNegativeButton("Annuler") { dialog, _ -> dialog.cancel() }

            // Show the AlertDialog
            builder.show()

            // Set the focus on the input field
            input.requestFocus()
        })
    }

    // Give folders name to view
    private fun getMyData(): Array<String> {
        return arrayOf(
            "Action", "Comédie", "Drame", "Fantastique", "Horreur", "Romance", "Science-fiction", "Tranche de vie"
        )
    }

    // Change page when click on a folder
    override fun onItemClick(folderPos: Int) {
        println("Clicked on $folderPos")

        val mainActivity = (activity as? BaseActivity)
        mainActivity?.changeFragment(WebtoonFolderDetailsFragment(folderPos))
        mainActivity?.changeTitle(getMyData()[folderPos])
    }
}