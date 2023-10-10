package com.example.playlistmaker.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist (
    val id: Int = 0,
    var name: String,
    var description: String = "",
    var placeholderPath: String = "",
    val tracksIdList: MutableList<String> = mutableListOf(),
    var tracksCount: Int = 0
    ) : Parcelable
