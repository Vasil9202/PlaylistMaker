package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.util.Resource

interface TracksRepository {
    fun searchTracks(expression: String): Resource<List<Track>>

}