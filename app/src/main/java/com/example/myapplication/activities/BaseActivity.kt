package com.example.myapplication.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.fragments.HomeFragment
import com.example.myapplication.fragments.LibraryFragment
import com.example.myapplication.fragments.SearchFragment
import com.example.myapplication.fragments.SettingsFragment
import com.example.myapplication.viewModels.UserViewModel
import com.google.firebase.auth.FirebaseAuth


class BaseActivity : AppCompatActivity() {
    private lateinit var tabs: Map<Int, Map<String, Any>>
    val  viewModel: UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        val  user = FirebaseAuth.getInstance().currentUser
        viewModel.user_id = user?.uid.toString()
        viewModel.email = user?.email.toString()
        Log.d("USER-DATA",viewModel.email+viewModel.user_id)
    }

    override fun onStart() {
        super.onStart()

        tabs = mapOf(
            R.id.activityBase_homeTab to mapOf(
                "fragment" to HomeFragment(), "title" to getString(R.string.home_tab_title)
            ), R.id.activityBase_searchTab to mapOf(
                "fragment" to SearchFragment(), "title" to getString(R.string.search_tab_title)
            ), R.id.activityBase_libraryTab to mapOf(
                "fragment" to LibraryFragment(), "title" to getString(R.string.library_tab_title)
            ), R.id.activityBase_settingsTab to mapOf(
                "fragment" to SettingsFragment(), "title" to getString(R.string.settings_tab_title)
            )
        )

        tabs.keys.forEach { id ->
            findViewById<View>(id).setOnClickListener { changeToTab(id) }
        }
    }

    private fun changeToTab(id: Int) {
        val tab = tabs[id]
        changeTitle(tab?.get("title") as String)
        changeFragment(tab["fragment"] as Fragment)
    }

    fun changeFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.activityBase_fragmentsContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun changeTitle(title: String) {
        findViewById<TextView>(R.id.activityBase_titleText).text = title
    }
}