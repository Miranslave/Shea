package com.example.myapplication.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.activities.BaseActivity
import com.example.myapplication.activities.CreateAccountActivity
import com.example.myapplication.activities.LoginActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private val subTabs = mapOf(
    R.id.activityBase_aboutText to mapOf(
        "fragment" to AboutFragment(), "title" to "À propos"
    ), R.id.activityBase_helpText to mapOf(
        "fragment" to HelpFragment(), "title" to "Aide"
    )
)
class SettingsFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        subTabs.keys.forEach { id ->
            view.findViewById<View>(id).setOnClickListener { changeToTab(id) }
        }

        view.findViewById<View>(R.id.activityBase_disconnect).setOnClickListener {
            showLoginActivity()
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

    private fun showLoginActivity() {
        Firebase.auth.signOut()
        val intent = Intent((activity as? BaseActivity), LoginActivity::class.java)
        startActivity(intent)
    }
}