package com.example.myapplication.network.Canvas.Genrelist


data class MessageGenrelist (
    val type: String,
    val service:String,
    val version: String,
    val result: com.example.myapplication.network.Canvas.Genrelist.ResultGenrelist
)