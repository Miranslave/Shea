package com.example.myapplication.image

import android.content.Context
import android.widget.ImageView
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient

class ImageLoader {
    private val client:OkHttpClient
    private val custom_picasso:Picasso
    private val preURL:String

    //  to instance you need to give the pre_url from webtoon
    //  and a context if you want you can go for a full custom double header in second  constructor
    constructor(preURL:String,context: Context){
        client =OkHttpClient.Builder()
            .addNetworkInterceptor { chain ->
                chain.proceed(
                    chain.request()
                        .newBuilder()
                        .header("Referer", "http://m.webtoons.com/").addHeader("User-Agent","Mozilla/5.0 (Linux; Android 8.1.0; Mi MIX 2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.125 Mobile Safari/537.36")
                        .build()
                )
            }
            .build()
        custom_picasso =Picasso.Builder(context).downloader(OkHttp3Downloader(client))
            .build()
        this.preURL = preURL
    }
    constructor(preURL:String,context: Context,header1key:String,header1value:String,header2key:String,header2value:String){
        client =OkHttpClient.Builder()
            .addNetworkInterceptor { chain ->
                chain.proceed(
                    chain.request()
                        .newBuilder()
                        .header(header1key, header1value).addHeader(header2key,header2value)
                        .build()
                )
            }
            .build()
        custom_picasso =Picasso.Builder(context).downloader(OkHttp3Downloader(client))
            .build()
        this.preURL = preURL
    }

    fun load(component: ImageView,url:String){
        custom_picasso.load(this.preURL+url).into(component)
    }
}