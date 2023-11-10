package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlin.reflect.typeOf

private val tabs = mapOf(
    R.id.home_tab to mapOf(
        "fragment" to HomeFragment(), "title" to "Accueil"
    ), R.id.search_tab to mapOf(
        "fragment" to SearchFragment(), "title" to "Recherche"
    ), R.id.library_tab to mapOf(
        "fragment" to LibraryFragment(), "title" to "Bibliothèque"
    ), R.id.settings_tab to mapOf(
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

        // Change the title
        findViewById<TextView>(R.id.titleText).text = tab?.get("title") as String

        // Change the fragment
//        val newFragment = when(id) {
//            R.id.home_tab -> HomeFragment()
//            R.id.search_tab -> SearchFragment()
//            else -> throw IllegalArgumentException("Invalid tab ID")
//        }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.home_fragments_container, tab["fragment"] as Fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}