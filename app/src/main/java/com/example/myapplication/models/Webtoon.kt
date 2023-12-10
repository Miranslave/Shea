package com.example.myapplication.models

// Class representing a Webtoon
class Webtoon {
    // Private variables representing the properties of a Webtoon
    private val id: Int
    private val author: String
    private val title: String
    private val synopsis: String
    private val genre: String
    private val linkUrl: String
    private val thumbnail: String
    private val theme: String
    private val totalEpisodeCount: Int
    private var episodesList: List<WebtoonEpisode>

    // Constructor for the Webtoon class
    constructor(id: Int, title: String, synopsis: String, author: String, genre: String, theme: String, thumbnail: String, linkUrl: String, totalEpisodeCount: Int) {
        this.id = id
        this.title = title
        this.synopsis = synopsis
        this.author = author
        this.genre = genre
        this.theme = theme
        this.thumbnail = thumbnail
        this.linkUrl = linkUrl
        this.totalEpisodeCount = totalEpisodeCount
        this.episodesList = listOf()
    }

    // Getter methods for the properties of a Webtoon
    fun getId(): Int {
        return id
    }

    fun getTitle(): String {
        return title
    }

    fun getSynopsis(): String {
        return synopsis
    }

    fun getAuthor(): String {
        return author
    }

    fun getGenre(): String {
        return genre
    }

    fun getTheme(): String {
        return theme
    }

    fun getThumbnail(): String {
        return thumbnail
    }

    fun getLinkUrl(): String {
        return linkUrl
    }

    fun getTotalEpisodeCount(): Int {
        return totalEpisodeCount
    }

    fun getEpisodesList(): List<WebtoonEpisode> {
        return episodesList
    }

    fun setEpisodesList(episodesList: List<WebtoonEpisode>) {
        this.episodesList = episodesList
    }

    // Method to return a string representation of a Webtoon
    override fun toString(): String {
        return "Webtoon(id=$id, title='$title', synopsis='$synopsis', author='$author', genre='$genre', theme='$theme', thumbnail='$thumbnail', linkUrl='$linkUrl')"
    }
}
