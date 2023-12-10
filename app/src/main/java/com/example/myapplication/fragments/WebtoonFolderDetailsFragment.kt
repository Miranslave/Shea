package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import com.example.myapplication.activities.BaseActivity
import com.example.myapplication.R
import com.example.myapplication.models.WebtoonFolder

class WebtoonFolderDetailsFragment(private val folder: WebtoonFolder) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_webtoon_folder_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the title
        val title: TextView = view.findViewById(R.id.fragmentWebtoonFolderDetails_titleTextField)
        title.text = folder.getTitle()
        title.isEnabled = false

        // Set the description
        val description: TextView = view.findViewById(R.id.fragmentWebtoonFolderDetails_descriptionText)
        description.text = folder.getDescription()

        // Set the back button to go back to the home page
        view.findViewById<TextView>(R.id.fragmentWebtoonFolderDetails_previousPageButton).setOnClickListener {
            (activity as? BaseActivity)?.changeFragment(HomeFragment())
            (activity as? BaseActivity)?.changeTitle(getString(R.string.home_tab_title))
        }

        // Set the edit button
        view.findViewById<AppCompatImageButton>(R.id.fragmentWebtoonFolderDetails_editButton).setOnClickListener {
            title.isEnabled = !title.isEnabled
        }
    }
}