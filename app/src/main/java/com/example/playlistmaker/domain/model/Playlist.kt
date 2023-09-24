package com.example.playlistmaker.domain.model

import androidx.room.PrimaryKey

data class Playlist (
    val id: Int = 0,
    val name: String,
    val description: String = "",
    val placeholderPath: String = "",
    val tracksIdList: MutableList<String> = mutableListOf(),
    var tracksCount: Int = 0
    )
