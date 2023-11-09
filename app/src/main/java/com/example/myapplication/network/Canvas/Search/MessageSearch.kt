package com.example.myapplication.network.Canvas.Search

data class MessageSearch (
    val type: String,
    val service:String,
    val version: String,
    val result: com.example.myapplication.network.Canvas.Search.ResultSearch
)
