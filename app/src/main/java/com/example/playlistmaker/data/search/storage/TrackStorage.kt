package com.example.playlistmaker.data.search.storage

import com.example.playlistmaker.data.search.storage.model.TracksList

interface TrackStorage {

    fun writeStorage(track: List<TracksList>)

    fun readStorage(): List<TracksList>

    fun clear()
}