package com.example.myapplication.network.Originals.TitleRankByGenre

data class TitleListByRankResult(
    val tabList:List<GenreItems>,
    val titleNoListByTabCode: List<TitleListByGenre>
)