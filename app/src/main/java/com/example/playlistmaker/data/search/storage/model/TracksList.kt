package com.example.playlistmaker.data.search.storage.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TracksList(
    val trackName: String,
    val artistName: String,
    val trackTimeMin: String,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
): Parcelable
