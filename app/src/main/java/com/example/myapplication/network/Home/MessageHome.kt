package com.example.myapplication.network.Home

import com.example.myapplication.network.Home.ResultHome

data class MessageHome(
    val type: String,
    val service:String,
    val version: String,
    val result: ResultHome
)
