package com.example.myapplication.viewModels

// Import necessary Android and project-specific classes
import android.util.Log
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.Webtoon
import com.example.myapplication.adapters.WebtoonsListAdapter
import com.example.myapplication.enums.Langage
import com.example.myapplication.network.WebtoonOriginalsApi
import com.example.myapplication.network.originals.titleList.OriginalRequestTitleList
import com.example.myapplication.network.originals.titleList.OriginalTitle
import com.example.myapplication.network.originals.titleList.OriginalTitleList
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// Define a ViewModel for the Library
class SearchViewModel : WebtoonViewModel() {
    private var lastTitleSearchString: String = ""
    private var lastTitleSearchCallback: ViewModelCallback<List<Webtoon>>? = null

    init {
        this.queryApiWebtoonsList()
    }

    // Function to get a list of Webtoons
    fun searchForWebtoon(webtoonTitle: String, callback: ViewModelCallback<List<Webtoon>>) {
        this.lastTitleSearchCallback = callback
        this.lastTitleSearchString = webtoonTitle

        if (this.webtoonsList.isNotEmpty())
            this.searchForWebtoonInList()
    }

    private fun queryApiWebtoonsList() {
        this.getRetrofitWebtoonsList(object : ViewModelCallback<List<Webtoon>> {
            override fun onSuccess(result: List<Webtoon>) {
                Log.d("Search list", "List fetched")
                searchForWebtoonInList()
            }

            // On error, log the error and show a toast message.
            override fun onError(e: Throwable) {
                Log.d("Search error", "Title: $lastTitleSearchString")
                lastTitleSearchCallback?.onError(e)
            }
        })
    }

    private fun searchForWebtoonInList() {
        if (this.lastTitleSearchCallback == null)
            return

        if (this.lastTitleSearchString.isEmpty()) {
            this.lastTitleSearchCallback!!.onSuccess(this.webtoonsList)
            return
        }

        Log.d("Search", "Title: ${this.lastTitleSearchString}")
        this.lastTitleSearchCallback!!.onSuccess(this.webtoonsList.filter { it.getTitle().contains(this.lastTitleSearchString, ignoreCase = true) })
    }
}
