package com.example.playlistmaker.data

import com.example.playlistmaker.domain.model.Track

data class TrackResponse(val resultCount: String,
                         val results: List<Track>)
