package com.example.playlistmaker.ui.db

import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface  FavouriteTrackInteractor {
    fun getfavouriteTracks(): Flow<List<Track>>

    fun getfavouriteTracksId(): Flow<List<String>>

}