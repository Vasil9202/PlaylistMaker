package com.example.playlistmaker.ui.search

import com.example.playlistmaker.domain.model.Track


sealed interface TracksState {

    object Loading : TracksState

    data class Content(
        val movies: List<Track>
    ) : TracksState

    data class Error(
        val errorMessage: String
    ) : TracksState

    data class Empty(
        val message: String
    ) : TracksState

}