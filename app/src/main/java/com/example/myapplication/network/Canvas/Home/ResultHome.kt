package com.example.myapplication.network.Canvas.Home

data class ResultHome(

    val votePeriodForRisingStar: Boolean,
    val weeklyHotTitleList: List<Titleinfo>,
    val popularByGenreList: List<Genre>,
    val weeklyHotByGenreList: List<Genre>

)
