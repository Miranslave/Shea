package com.example.myapplication.viewModels

// Import necessary Android and project-specific classes
import android.util.Log
import com.example.myapplication.models.Webtoon
import com.example.myapplication.network.WebtoonApiController

// Define a ViewModel for the Library
class SearchViewModel : CustomViewModel() {
    private val webtoonApiController = WebtoonApiController.getInstance()
    private var lastTitleSearchString: String = ""
    private var lastTitleSearchCallback: ViewModelCallback<List<Webtoon>>? = null

    init {
        this.queryApiWebtoonsList()
    }

    // Function to search for a Webtoon in the list
    fun searchForWebtoon(webtoonTitle: String, callback: ViewModelCallback<List<Webtoon>>) {
        this.lastTitleSearchCallback = callback
        this.lastTitleSearchString = webtoonTitle

        if (this.webtoonApiController.getWebtoonsList().isNotEmpty())
            this.searchForWebtoonInList()
    }

    // Function to get a list of Webtoons
    fun getWebtoonList(callback: ViewModelCallback<List<Webtoon>>) {
        return this.webtoonApiController.getRetrofitWebtoonsList(callback)
    }

    private fun queryApiWebtoonsList() {
        this.webtoonApiController.getRetrofitWebtoonsList(object : ViewModelCallback<List<Webtoon>> {
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
            this.lastTitleSearchCallback!!.onSuccess(this.webtoonApiController.getWebtoonsList())
            return
        }

        Log.d("Search", "Title: ${this.lastTitleSearchString}")
        this.lastTitleSearchCallback!!.onSuccess(this.webtoonApiController.getWebtoonsList().filter { it.getTitle().contains(this.lastTitleSearchString, ignoreCase = true) })
    }
}
