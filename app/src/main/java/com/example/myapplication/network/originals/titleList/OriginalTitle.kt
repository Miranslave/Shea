package com.example.myapplication.network.originals.titleList

data class OriginalTitle(
    val titleNo: Int,
    val language: String,
    val title: String,
    val koreanTitle: String,
    val representGenre: String,
    val theme: String,
    val registerYmdt: String,
    val unsuitableForChildren: Boolean,
    val webnovel: Boolean,
    val thumbnail: String,
    val starScoreAverage: Float,
    val readCount: Int,
    val favoriteCount: Int,
    val lastEpisodeRegisterYmdt: String,
    val synopsis: String,
    val subGenre: List<String>,
    val weekday: List<String>,
    val genreColor: String,
    val webtoonType: String,
    val writingAuthorName: String,

    )