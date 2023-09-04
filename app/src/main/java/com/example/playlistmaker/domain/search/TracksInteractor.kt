package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun searchTracks(expression: String) : Flow<Pair<List<Track>?, String?>>

    fun writeStorage(track: List<Track>)

    fun readStorage(): List<Track>

    fun clear()
}