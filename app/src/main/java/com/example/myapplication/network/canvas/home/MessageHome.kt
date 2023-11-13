package com.example.myapplication.network.canvas.home

data class MessageHome(
    val type: String,
    val service: String,
    val version: String,
    val result: ResultHome,
)
