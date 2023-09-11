package com.example.playlistmaker.data.impl

import com.example.playlistmaker.data.converters.TrackDbConvertor
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.domain.db.FavouriteTrackRepository
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouriteTrackRepositoryImpl (
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor
    ) : FavouriteTrackRepository {

    override fun getfavouriteTracks(): Flow<List<Track>> = flow {
            val tracks = appDatabase.trackDao().getTracks()
            emit(convertFromTrackEntity(tracks))
        }

    override fun getfavouriteTracksId(): Flow<List<String>> = flow {
        val tracks = appDatabase.trackDao().getTracksId()
        emit(tracks)
    }

        private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
            return tracks.map { track -> trackDbConvertor.map(track) }
        }
    }
