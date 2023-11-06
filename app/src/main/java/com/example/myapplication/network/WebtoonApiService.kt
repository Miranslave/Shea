package com.example.myapplication.network

import com.example.myapplication.network.Home.RequestHome
import com.example.myapplication.network.Search.RequestSearch
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

    //Fonction qui renvoie le JSON de la page d'accueil, les deux headers son
    // les clé d'accées à l'API et l'adresse de l'host
    @Headers("X-RapidAPI-Key: 83abec409emshdaef86121bb4e5ep125787jsnd79326c26832", "X-RapidAPI-Host: webtoon.p.rapidapi.com")
    @GET("home?language=en")
    suspend fun getHome(): RequestHome

    // Trouver comment mettre avec retrofit2 les paramètres dans l'url
    // comme le string de recherche, la taille des pages
    @Headers("X-RapidAPI-Key: 83abec409emshdaef86121bb4e5ep125787jsnd79326c26832", "X-RapidAPI-Host: webtoon.p.rapidapi.com")
    @GET("search?query=boy%20friend&startIndex=0&pageSize=5&language=en")
    suspend fun getSearch(): RequestSearch


}
object WebtoonApi {
    val retrofitService : WebtoonApiService by lazy {
        retrofit.create(WebtoonApiService::class.java) }
}