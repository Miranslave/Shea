package com.example.myapplication.network.canvas.titleList

data class MessageTitleList(
    val type: String,
    val service: String,
    val version: String,
    val result: ResultTitleList,
)