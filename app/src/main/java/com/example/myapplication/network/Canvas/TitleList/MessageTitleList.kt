package com.example.myapplication.network.Canvas.TitleList

data class MessageTitleList (
    val type:String,
    val service: String,
    val  version: String,
    val  result: com.example.myapplication.network.Canvas.TitleList.ResultTitleList
)