package com.example.myapplication.network

import com.example.myapplication.enums.Langage
import com.example.myapplication.models.Webtoon
import com.example.myapplication.network.originals.titleList.OriginalRequestTitleList
import com.example.myapplication.network.originals.titleList.OriginalTitleList
import com.example.myapplication.viewModels.CustomViewModel
import com.example.myapplication.viewModels.ViewModelCallback

class WebtoonApiController private constructor() : CustomViewModel() {

    private var webtoonsList: List<Webtoon> = ArrayList()
    private var isRequestInProgress = false
    private var waitingCallbacks: List<ViewModelCallback<List<Webtoon>>> = ArrayList()

    // Function to get a list of Webtoons
    fun getRetrofitWebtoonsList(callback: ViewModelCallback<List<Webtoon>>) {
        // Avoid making a network request if the list of Webtoons has already been retrieved
        if (webtoonsList.isNotEmpty()) {
            callback.onSuccess(webtoonsList)
            return
        }

        // If the list of Webtoons has not been retrieved yet but a network request is already in progress, wait for the network request to finish
        if (isRequestInProgress) {
            waitingCallbacks += callback
            return
        }

        this.isRequestInProgress = true
        executeInCoroutineScope(WebtoonOriginalsApi.retrofitService::getTitlesList, listOf(Langage.en), object : ViewModelCallback<OriginalRequestTitleList> {
            // Handle successful network request
            override fun onSuccess(result: OriginalRequestTitleList) {
                val titlesListMessage: OriginalTitleList = result.message.result.titleList

                if (titlesListMessage.titles.isEmpty()) {
                    callback.onError(Exception("Aucun résultat"))
                    return
                }

                webtoonsList = titlesListMessage.titles.map {
                    Webtoon(it.titleNo, it.title, it.synopsis, it.writingAuthorName, it.representGenre, it.theme, it.thumbnail, "https://www.webtoons.com/fr/osef/" + it.titleForSeo + "/list?title_no=" + it.titleNo, it.totalServiceEpisodeCount)
                }

                // Callback with a list of Webtoons
                callback.onSuccess(webtoonsList)
                waitingCallbacks.forEach { it.onSuccess(webtoonsList) }
                isRequestInProgress = false
            }

            // Handle error in network request
            override fun onError(e: Throwable) {
                // Catch IllegalStateExceptions because it means that the network request was successful but the response was not successfully shown
                // This is because the user changed the fragment before the response was shown
                if (e is IllegalStateException)
                    return

                callback.onError(e)
                waitingCallbacks.forEach { it.onError(e) }
                isRequestInProgress = false
            }
        })
    }

    fun getWebtoonsList(): List<Webtoon> {
        return webtoonsList
    }

    companion object {
        @Volatile
        private var INSTANCE: WebtoonApiController? = null

        fun getInstance(): WebtoonApiController {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = WebtoonApiController()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}