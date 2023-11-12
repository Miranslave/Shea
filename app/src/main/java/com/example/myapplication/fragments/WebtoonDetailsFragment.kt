package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myapplication.activities.BaseActivity
import com.example.myapplication.R
import com.example.myapplication.Webtoon

class WebtoonDetailsFragment(private val webtoon: Webtoon) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_webtoon_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.fragmentWebtoonDetails_previousPageButton).setOnClickListener {
            (activity as? BaseActivity)?.changeFragment(LibraryFragment())
            (activity as? BaseActivity)?.changeTitle(getString(R.string.library_tab_title))
        }
    }
}