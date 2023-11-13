package com.example.myapplication.network.originals.titleRankByGenre

data class TitleListByRankMessage(
    val type: String,
    val service: String,
    val version: String,
    val result: TitleListByRankResult,
)