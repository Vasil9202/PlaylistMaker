package com.example.playlistmaker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_track_table")
data class PlaylistTrackEntity (
    @PrimaryKey
        val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMin: String,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    val timeInsert: Long = System.currentTimeMillis()
)

