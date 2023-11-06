package com.example.myapplication.network.TitleList

data class TitleList (
    val titles: List<TitleInfoSyn>,
    val count: Int,
    val now: String
)