package com.example.playlistmaker.data.search.dto

data class TrackSearchResponse(val resultCount: String, val results: List<TrackDto>) : Response()
