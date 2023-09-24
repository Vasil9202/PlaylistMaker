package com.example.playlistmaker.data.search.dto

import java.text.SimpleDateFormat
import java.util.Locale

data class TrackDto(
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String?,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
) {
    fun trackTimeMin() : String{
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis.toLong())
    }
}