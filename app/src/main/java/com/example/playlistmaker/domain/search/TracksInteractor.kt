package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.model.Track

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTracks: List<Track>?, errorMessage: String?)
    }

    fun writeStorage(track: List<Track>)

    fun readStorage(): List<Track>

    fun clear()
}