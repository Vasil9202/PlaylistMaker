package com.example.playlistmaker.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date
import java.time.LocalDateTime

@Entity(tableName = "favourite_track_table")
data class TrackEntity (
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

