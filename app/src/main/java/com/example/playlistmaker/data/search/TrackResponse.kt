package com.example.playlistmaker.data.search

import com.example.playlistmaker.domain.search.model.Track

data class TrackResponse(val resultCount: String,
                         val results: List<Track>)
