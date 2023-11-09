package com.example.myapplication.network

import com.example.myapplication.enums.Langage
import com.example.myapplication.network.Canvas.TitleList.RequestTitleList
import com.example.myapplication.network.Originals.GenreList.OriginalRequestGenreList
import com.example.myapplication.network.Originals.TitleList.OriginalRequestTitleList
import com.example.myapplication.network.Originals.TitleList.OriginalResultTitleList
import com.example.myapplication.network.Originals.TitleList.OriginalTitleList
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


const val ORIGINAL_URL =
    "https://webtoon.p.rapidapi.com/originals/"


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(ORIGINAL_URL)
    .build()

interface WebtoonOriginalsApiService {
    @Headers(
        "X-RapidAPI-Key: 83abec409emshdaef86121bb4e5ep125787jsnd79326c26832",
        "X-RapidAPI-Host: webtoon.p.rapidapi.com"
    )
    @GET("genres/list")
    suspend fun getGenresList(
        @Query("lang") lang: Langage
    ): OriginalRequestGenreList
    @Headers(
        "X-RapidAPI-Key: 83abec409emshdaef86121bb4e5ep125787jsnd79326c26832",
        "X-RapidAPI-Host: webtoon.p.rapidapi.com"
    )
    @GET("titles/list")
    suspend fun getTitlesList(
        @Query("lang") lang: Langage
    ): OriginalRequestTitleList
}

object WebtoonOriginalsApi {
    val retrofitService: WebtoonOriginalsApiService by lazy {
        retrofit.create(WebtoonOriginalsApiService::class.java)
    }
}