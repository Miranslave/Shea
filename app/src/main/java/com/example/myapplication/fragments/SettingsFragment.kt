package com.example.myapplication.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.activities.BaseActivity
import com.example.myapplication.activities.LoginActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingsFragment : Fragment() {

    private lateinit var subTabs: Map<Int, Map<String, Any>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        this.subTabs = mapOf(
            R.id.activityBase_aboutText to mapOf(
                "fragment" to AboutFragment(), "title" to getString(R.string.about)
            ), R.id.activityBase_helpText to mapOf(
                "fragment" to HelpFragment(), "title" to getString(R.string.help)
            )
        )

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        // Set click listeners for each sub tab
        subTabs.keys.forEach { id ->
            view.findViewById<View>(id).setOnClickListener { changeToTab(id) }
        }

        // Set click listener for disconnect button
        view.findViewById<View>(R.id.activityBase_disconnect).setOnClickListener {
            disconnectFromApp()
        }

        return view
    }

    private fun changeToTab(id: Int) {
        val tab = subTabs[id]
        println("clicked on" + id)
        val mainActivity = (activity as? BaseActivity)

        mainActivity?.changeTitle(tab?.get("title") as String)
        mainActivity?.changeFragment(tab?.get("fragment") as Fragment)
    }

    private fun disconnectFromApp() {
        Firebase.auth.signOut()

        // Remove shared preferences
        val sharedPreferences = (activity as? BaseActivity)?.getSharedPreferences("MyApp", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putBoolean("isUserLoggedIn", false)
        editor?.apply()

        val intent = Intent((activity as? BaseActivity), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        activity?.finish()
    }
}