package com.example.myapplication.network.originals.titleRankByGenre

data class TitleListByRankResult(
    val tabList: List<GenreItems>,
    val titleNoListByTabCode: List<TitleListByGenre>,
)