package com.example.myapplication.network.Canvas.TitleInfo

import com.example.myapplication.network.Canvas.TitleList.TitleInfoSyn


data class MessageTitleInfo (
    val type: String,
    val service: String,
    val  version: String,
    val  result: com.example.myapplication.network.Canvas.TitleInfo.ResultTitleInfo
)