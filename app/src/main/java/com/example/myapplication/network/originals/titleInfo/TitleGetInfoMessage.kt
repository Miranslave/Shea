package com.example.myapplication.network.originals.titleInfo

data class TitleGetInfoMessage(
    val type: String,
    val service: String,
    val version: String,
    val result: TitleGetInfoResult,
)