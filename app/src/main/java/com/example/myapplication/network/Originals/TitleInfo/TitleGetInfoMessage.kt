package com.example.myapplication.network.Originals.TitleInfo

import com.example.myapplication.network.Canvas.Home.Titleinfo

data class TitleGetInfoMessage (
    val type:String,
    val service:String,
    val version:String,
    val result: TitleGetInfoResult
)