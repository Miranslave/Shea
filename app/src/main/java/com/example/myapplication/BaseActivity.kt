package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

private val tabsFragments = mapOf(
    R.id.home_tab to mapOf(
        "title" to "Accueil"
    ),
    R.id.search_tab to mapOf(
        "title" to "Recherche"
    )
)

class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        arrayOf(
            R.id.home_tab,
            R.id.search_tab).forEach { id -> findViewById<View>(id).setOnClickListener { changeToTab(id) }
        }
    }

    private fun changeToTab(id: Int) {
        val tab = tabsFragments[id]

        // Change the title
        findViewById<TextView>(R.id.titleText).text = tab?.get("title") as String

        // Change the fragment
        val newFragment = when(id) {
            R.id.home_tab -> HomeFragment()
            R.id.search_tab -> SearchFragment()
            else -> throw IllegalArgumentException("Invalid tab ID")
        }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.home_fragments_container, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}