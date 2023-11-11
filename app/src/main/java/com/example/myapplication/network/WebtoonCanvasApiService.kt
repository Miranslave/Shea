package com.example.myapplication.network

import com.example.myapplication.enums.Langage
import com.example.myapplication.network.Canvas.Genrelist.RequestGenreList
import com.example.myapplication.network.Canvas.Home.RequestHome
import com.example.myapplication.network.Canvas.Search.RequestSearch
import com.example.myapplication.network.Canvas.TitleInfo.RequestTitleInfo
import com.example.myapplication.network.Canvas.TitleList.RequestTitleList
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


const val language = "language=fr"
const val BASE_URL =
    "https://webtoon.p.rapidapi.com/canvas/"
const val XRapidAPIHostadress =
    "webtoon.p.rapidapi.com"
const val XRapidAPIkey =
    "83abec409emshdaef86121bb4e5ep125787jsnd79326c26832"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface WebtoonCanvasApiService {

    // les deux headers sont
    // les clé d'accées à l'API et l'adresse de l'host
    // Trouver comment mettre avec retrofit2 les paramètres dans l'url
    // comme le string de recherche, la taille des pages


    //Fonction qui renvoie le JSON qui donne la page home
    @Headers(XRapidAPIkey, XRapidAPIHostadress)
    @GET("home?language=en")
    suspend fun getHome(): RequestHome


    //Fonction qui renvoie le JSON qui donne la page home
    @Headers(XRapidAPIkey, XRapidAPIHostadress)
    @GET("search")
    suspend fun getSearch(
        @Query("query") searchquery: String,
        @Query("startIndex") debutpage: Int,
        @Query("pageSize") nbtitles: Int,
        @Query("language") lang: Langage
    ):RequestSearch

    @Headers(XRapidAPIkey, XRapidAPIHostadress)
    @GET("genres/list")
    suspend fun getGenresList(
        @Query("lang") lang: Langage
    ): RequestGenreList

    @Headers(XRapidAPIkey, XRapidAPIHostadress)
    @GET("titles/list")
    suspend fun getTitlesList(
        //The value of code field returned in …/canvas/genres/list endpoint.
        @Query("genre") genre: String,
        //One of the following : UPDATE|READ_COUNT|LIKEIT
        @Query("sortOrder") sortOrder: String,
        @Query("startIndex") debutpage: Int,
        @Query("pageSize") nbtitles: Int,
        @Query("lang") lang: Langage
    ): RequestTitleList

    @Headers(XRapidAPIkey, XRapidAPIHostadress)
    @GET("titles/get-info")
    suspend fun getTitleInfo(
        @Query("titleNo") titleNo: String,
        @Query("lang") lang: Langage
    ): RequestTitleInfo

    // Optional
    @Headers(XRapidAPIkey, XRapidAPIHostadress)
    @GET("titles/get-recommend")
    suspend fun getTitleRecommend(
        @Query("titleNo") titleNo: String,
        @Query("lang") lang: Langage
    ): Unit

}

object WebtoonCanvasApi {
    val retrofitService: WebtoonCanvasApiService by lazy {
        retrofit.create(WebtoonCanvasApiService::class.java)
    }
}