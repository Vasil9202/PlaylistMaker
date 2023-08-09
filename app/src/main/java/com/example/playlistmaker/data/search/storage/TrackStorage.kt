package com.example.playlistmaker.data.search.storage

import com.example.playlistmaker.data.search.storage.model.TracksList
import com.example.playlistmaker.domain.model.Track

interface TrackStorage {

    fun writeStorage(track: List<TracksList>)

    fun readStorage(): List<TracksList>

    fun clear()
}