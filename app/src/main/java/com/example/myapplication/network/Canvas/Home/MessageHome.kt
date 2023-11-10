package com.example.myapplication.network.Canvas.Home

import com.example.myapplication.network.Canvas.Home.ResultHome

data class MessageHome(
    val type: String,
    val service:String,
    val version: String,
    val result: com.example.myapplication.network.Canvas.Home.ResultHome
)
