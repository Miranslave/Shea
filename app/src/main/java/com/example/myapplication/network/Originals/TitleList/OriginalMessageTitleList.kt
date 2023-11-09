package com.example.myapplication.network.Originals.TitleList

data class OriginalMessageTitleList (
    val type:String,
    val service:String,
    val version:String,
    val  result:OriginalResultTitleList
)