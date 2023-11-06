package com.example.myapplication.network.TitleInfo

import com.example.myapplication.network.TitleList.TitleInfoSyn


data class MessageTitleInfo (
    val type: String,
    val service: String,
    val  version: String,
    val  result: ResultTitleInfo
)