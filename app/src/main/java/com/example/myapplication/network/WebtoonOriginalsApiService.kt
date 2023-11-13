package com.example.myapplication.network

import com.example.myapplication.enums.Langage
import com.example.myapplication.network.originals.genreList.OriginalRequestGenreList
import com.example.myapplication.network.originals.titleInfo.TitleGetInfoRequest
import com.example.myapplication.network.originals.titleList.OriginalRequestTitleList
import com.example.myapplication.network.originals.titleRankByGenre.TitleListByRankRequest
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


const val ORIGINAL_URL = "https://webtoon.p.rapidapi.com/originals/"
const val Headerkey = "X-RapidAPI-Key: 83abec409emshdaef86121bb4e5ep125787jsnd79326c26832"
const val Headerhost = "X-RapidAPI-Host: webtoon.p.rapidapi.com"

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(ORIGINAL_URL).build()

interface WebtoonOriginalsApiService {
    @Headers(Headerkey, Headerhost)
    @GET("genres/list")
    suspend fun getGenresList(
        @Query("lang") lang: Langage,
    ): OriginalRequestGenreList

    @Headers(Headerkey, Headerhost)
    @GET("titles/list")
    suspend fun getTitlesList(
        @Query("lang") lang: Langage,
    ): OriginalRequestTitleList

    @Headers(Headerkey, Headerhost)
    @GET("titles/list-by-rank")
    suspend fun getTitlesListByRank(
        @Query("count") count: Int,
        @Query("lang") lang: Langage,
    ): TitleListByRankRequest

    @Headers(Headerkey, Headerhost)
    @GET("titles/get-info")
    suspend fun getTitleInfo(
        @Query("titleNo") titleNo: Int,
        @Query("lang") lang: Langage,
    ): TitleGetInfoRequest
}

object WebtoonOriginalsApi {
    val retrofitService: WebtoonOriginalsApiService by lazy {
        retrofit.create(WebtoonOriginalsApiService::class.java)
    }
}