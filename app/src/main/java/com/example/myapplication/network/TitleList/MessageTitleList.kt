package com.example.myapplication.network.TitleList

data class MessageTitleList (
    val type:String,
    val service: String,
    val  version: String,
    val  result: ResultTitleList
)