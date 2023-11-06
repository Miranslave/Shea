package com.example.myapplication.network.Genrelist


data class MessageGenrelist (
    val type: String,
    val service:String,
    val version: String,
    val result: ResultGenrelist
)