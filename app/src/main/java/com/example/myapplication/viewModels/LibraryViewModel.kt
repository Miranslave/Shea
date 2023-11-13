package com.example.myapplication.viewModels

// Import necessary Android and project-specific classes
import android.util.Log
import com.example.myapplication.Webtoon
import com.example.myapplication.enums.Langage
import com.example.myapplication.network.WebtoonOriginalsApi
import com.example.myapplication.network.originals.titleInfo.TitleGetInfoRequest

// Define a ViewModel for the Library
class LibraryViewModel : CustomViewModel() {

    // Define a list of Webtoon IDs
    private var webtoonsIdsList: Array<Int> = arrayOf(5888)

    // Function to get a list of Webtoons
    fun getWebtoonsList(callback: ViewModelCallback<Array<Webtoon>>) {
        Log.d("info", "getWebtoonsList called")

        // Execute a network request in a coroutine scope
        executeInCoroutineScope(WebtoonOriginalsApi.retrofitService::getTitleInfo, listOf(webtoonsIdsList[0], Langage.en), object : ViewModelCallback<TitleGetInfoRequest> {
            // Handle successful network request
            override fun onSuccess(result: TitleGetInfoRequest) {
                val infos = result.message.result.titleInfo
                // Callback with a list of Webtoons
                callback.onSuccess(
                    arrayOf(
                        Webtoon(infos.titleNo, infos.title, infos.synopsis, infos.writingAuthorName, infos.representGenre, infos.restTerminationStatus, infos.thumbnail, infos.linkUrl, infos.totalServiceEpisodeCount)
                    )
                )
            }

            // Handle error in network request
            override fun onError(e: Throwable) {
                error(e)
                callback.onError(e)
            }
        })
    }
}
