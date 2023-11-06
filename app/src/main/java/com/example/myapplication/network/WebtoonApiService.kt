package com.example.myapplication.network

import com.example.myapplication.network.Genrelist.RequestGenreList
import com.example.myapplication.network.Genrelist.ResultGenrelist
import com.example.myapplication.network.Home.RequestHome
import com.example.myapplication.network.Search.RequestSearch
import com.example.myapplication.network.TitleInfo.RequestTitleInfo
import com.example.myapplication.network.TitleList.RequestTitleList
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers


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
interface WebtoonApiService {

    // les deux headers sont
    // les clé d'accées à l'API et l'adresse de l'host
    // Trouver comment mettre avec retrofit2 les paramètres dans l'url
    // comme le string de recherche, la taille des pages

    //Fonction qui renvoie le JSON qui donne la page home
    @Headers("X-RapidAPI-Key: 83abec409emshdaef86121bb4e5ep125787jsnd79326c26832", "X-RapidAPI-Host: webtoon.p.rapidapi.com")
    @GET("home?language=en")
    suspend fun getHome(): RequestHome


    //Fonction qui renvoie le JSON qui donne la page home
    @Headers("X-RapidAPI-Key: 83abec409emshdaef86121bb4e5ep125787jsnd79326c26832", "X-RapidAPI-Host: webtoon.p.rapidapi.com")
    @GET("search?query=boy%20friend&startIndex=0&pageSize=5&language=en")
    suspend fun getSearch(): RequestSearch
    @Headers("X-RapidAPI-Key: 83abec409emshdaef86121bb4e5ep125787jsnd79326c26832", "X-RapidAPI-Host: webtoon.p.rapidapi.com")
    @GET("genres/list?language=en")
    suspend fun getGenresList(): RequestGenreList
    @Headers("X-RapidAPI-Key: 83abec409emshdaef86121bb4e5ep125787jsnd79326c26832", "X-RapidAPI-Host: webtoon.p.rapidapi.com")
    @GET("titles/list?genre=ALL&sortOrder=READ_COUNT&startIndex=0&pageSize=5&language=en")
    suspend fun getTitlesList(): RequestTitleList
    @Headers("X-RapidAPI-Key: 83abec409emshdaef86121bb4e5ep125787jsnd79326c26832", "X-RapidAPI-Host: webtoon.p.rapidapi.com")
    @GET("titles/get-info?titleNo=300138&language=en")
    suspend fun getTitleInfo(): RequestTitleInfo
}
object WebtoonApi {
    val retrofitService : WebtoonApiService by lazy {
        retrofit.create(WebtoonApiService::class.java) }
}