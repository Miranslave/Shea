package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class WebtoonFolderDetailsFragment(private val folderId: Int) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_webtoon_folder_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("Folder id: $folderId, should load infos here")

        view.findViewById<TextView>(R.id.previousPageButton).setOnClickListener {
            (activity as? BaseActivity)?.changeFragment(HomeFragment())
            (activity as? BaseActivity)?.changeTitle("Accueil")
        }
    }
}