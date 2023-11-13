package com.example.myapplication.network.originals.titleList

data class OriginalMessageTitleList(
    val type: String,
    val service: String,
    val version: String,
    val result: OriginalResultTitleList,
)