package com.example.playlistmaker.domain.db.impl

import com.example.playlistmaker.domain.db.FavouriteTrackRepository
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.db.FavouriteTrackInteractor
import kotlinx.coroutines.flow.Flow

class FavouriteTrackInteractorImpl(private val historyRepository: FavouriteTrackRepository) :
    FavouriteTrackInteractor {
    override fun getfavouriteTracks(): Flow<List<Track>> {
        return historyRepository.getfavouriteTracks()
    }

    override fun getfavouriteTracksId(): Flow<List<String>> {
        return historyRepository.getfavouriteTracksId()
    }
}