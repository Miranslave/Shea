package com.example.myapplication.network.Originals.TitleRankByGenre

data class TitleListByRankMessage (
    val type:String,
    val service:String,
    val version:String,
    val result: TitleListByRankResult
)