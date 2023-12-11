package com.example.myapplication.viewModels

// Import necessary Android and project-specific classes
import com.example.myapplication.models.Webtoon
import com.example.myapplication.network.WebtoonApiController

// Define a ViewModel for the Library
class WebtoonFolderDetailsViewModel : CustomViewModel() {
    private val webtoonApiController = WebtoonApiController.getInstance()

    // Function to get a list of Webtoons
    fun getWebtoonsList(filetingIdsList: List<Int>, callback: ViewModelCallback<List<Webtoon>>) {
        webtoonApiController.getRetrofitWebtoonsList(object : ViewModelCallback<List<Webtoon>> {
            // On successful fetch, update the RecyclerView with the fetched data.
            override fun onSuccess(result: List<Webtoon>) {
                callback.onSuccess(result.filter { filetingIdsList.contains(it.getId()) })
            }

            // On error, log the error and show a toast message.
            override fun onError(e: Throwable) {
                callback.onError(e)
            }
        })
    }
}
