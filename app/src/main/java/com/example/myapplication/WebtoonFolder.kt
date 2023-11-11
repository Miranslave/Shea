package com.example.myapplication

class WebtoonFolder {
    private val title: String
    private val description: String
    private val webtoons: ArrayList<Webtoon>

    constructor(title: String, description: String) {
        this.title = title
        this.description = description
        this.webtoons = ArrayList()
    }

    fun getTitle(): String {
        return title
    }

    fun getDescription(): String {
        return description
    }

    fun getWebtoons(): ArrayList<Webtoon> {
        return webtoons
    }

    fun addWebtoon(webtoon: Webtoon) {
        webtoons.add(webtoon)
    }

    fun removeWebtoon(webtoon: Webtoon) {
        webtoons.remove(webtoon)
    }

    override fun toString(): String {
        return "WebtoonFolder(title='$title', description='$description')"
    }
}