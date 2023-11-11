package com.example.myapplication

class Webtoon {
    private val title: String
    private val description: String

    constructor(title: String, description: String) {
        this.title = title
        this.description = description
    }

    fun getTitle(): String {
        return title
    }

    fun getDescription(): String {
        return description
    }

    override fun toString(): String {
        return "Webtoon(title='$title', description='$description')"
    }
}