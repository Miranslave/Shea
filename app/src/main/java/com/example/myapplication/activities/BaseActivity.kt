package com.example.myapplication.activities

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.fragments.HomeFragment
import com.example.myapplication.fragments.LibraryFragment
import com.example.myapplication.fragments.SearchFragment
import com.example.myapplication.fragments.SettingsFragment

private val tabs = mapOf(
    R.id.activityBase_homeTab to mapOf(
        "fragment" to HomeFragment(), "title" to "Accueil"
    ), R.id.activityBase_searchTab to mapOf(
        "fragment" to SearchFragment(), "title" to "Recherche"
    ), R.id.activityBase_libraryTab to mapOf(
        "fragment" to LibraryFragment(), "title" to "Bibliothèque"
    ), R.id.activityBase_settingsTab to mapOf(
        "fragment" to SettingsFragment(), "title" to "Paramètres"
    )
)

class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

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