package com.example.myapplication.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.R
import com.example.myapplication.WebtoonFolder
import com.example.myapplication.activities.BaseActivity
import com.example.myapplication.adapters.RecyclerViewEventsManager
import com.example.myapplication.adapters.WebtoonsFoldersListAdapter
import com.example.myapplication.adapters.WebtoonsRecyclerViewHolder
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : FragmentRecyclerViewManager(), RecyclerViewEventsManager {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_home, container, false)

        // Set up the RecyclerView with a grid layout to display folders in 2 columns
        this.initRecyclerViewDisplay(view, R.id.fragmentHome_itemsList, WebtoonsFoldersListAdapter(this.getMyData(), this, R.layout.item_webtoon_folder), GridLayoutManager(context, 2))

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
            }

            builder.setNegativeButton("Annuler") { dialog, _ -> dialog.cancel() }

            // Show the AlertDialog
            builder.show()

            // Set the focus on the input field
            input.requestFocus()
        })
    }

    // Give folders name to view
    private fun getMyData(): Array<Any> {
        return arrayOf(
            WebtoonFolder("Action", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget ultricies ultrices, nunc nisl aliquam nunc, quis aliquet nisl nunc eu nisl. Donec euismod, nisl eget ultricies ultrices, nunc nisl aliquam nunc, quis aliquet nisl nunc eu nisl."),
            WebtoonFolder("Aventure", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget ultricies ultrices, nunc nisl aliquam nunc, quis aliquet nisl nunc eu nisl. Donec euismod, nisl eget ultricies ultrices, nunc nisl aliquam nunc, quis aliquet nisl nunc eu nisl."),
            WebtoonFolder("Comédie", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget ultricies ultrices, nunc nisl aliquam nunc, quis aliquet nisl nunc eu nisl. Donec euismod, nisl eget ultricies ultrices, nunc nisl aliquam nunc, quis aliquet nisl nunc eu nisl."),
            WebtoonFolder("Drame", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget ultricies ultrices, nunc nisl aliquam nunc, quis aliquet nisl nunc eu nisl. Donec euismod, nisl eget ultricies ultrices, nunc nisl aliquam nunc, quis aliquet nisl nunc eu nisl."),
            WebtoonFolder("Fantastique", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget ultricies ultrices, nunc nisl aliquam nunc, quis aliquet nisl nunc eu nisl. Donec euismod, nisl eget ultricies ultrices, nunc nisl aliquam nunc, quis aliquet nisl nunc eu nisl."),
            WebtoonFolder("Horreur", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget ultricies ultrices, nunc nisl aliquam nunc, quis aliquet nisl nunc eu nisl. Donec euismod, nisl eget ultricies ultrices, nunc nisl aliquam nunc, quis aliquet nisl nunc eu nisl."),
            WebtoonFolder("Romance", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget ultricies ultrices, nunc nisl aliquam nunc, quis aliquet nisl nunc eu nisl. Donec euismod, nisl eget ultricies ultrices, nunc nisl aliquam nunc, quis aliquet nisl nunc eu nisl.")
        )
    }

    // Change page when click on a folder
    override fun onItemClick(position: Int, item: Any?) {
        Log.d("info", "Clicked on $position")

        val mainActivity = (activity as? BaseActivity)
        val webtoonFolder = item as WebtoonFolder

        mainActivity?.changeFragment(WebtoonFolderDetailsFragment(webtoonFolder))
        mainActivity?.changeTitle(webtoonFolder.getTitle())
    }

    // Set each item folder title to the view
    override fun onItemDraw(holder: WebtoonsRecyclerViewHolder, position: Int, item: Any?) {
        val webtoonFolder = item as WebtoonFolder
        holder.view.findViewById<TextView>(R.id.itemFolder_title).text = webtoonFolder.getTitle()
    }
}