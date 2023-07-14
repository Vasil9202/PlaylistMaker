package com.example.playlistmaker.data.dto

data class TrackSearchResponse(val resultCount: String, val results: List<TrackDto>) : Response()
