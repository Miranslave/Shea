package com.example.myapplication.network.canvas.titleInfo


data class MessageTitleInfo(
    val type: String,
    val service: String,
    val version: String,
    val result: ResultTitleInfo,
)