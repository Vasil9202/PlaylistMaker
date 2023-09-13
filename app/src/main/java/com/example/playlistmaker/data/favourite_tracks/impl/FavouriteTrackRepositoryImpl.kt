package com.example.playlistmaker.data.favourite_tracks.impl

import com.example.playlistmaker.data.converters.TrackDbMapper
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.domain.db.FavouriteTrackRepository
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FavouriteTrackRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbMapper: TrackDbMapper
) : FavouriteTrackRepository {

    override fun getfavouriteTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(convertFromTrackEntity(tracks))
    }.flowOn(Dispatchers.IO)

    override fun getfavouriteTracksId(): Flow<List<String>> = flow {
        val tracks = appDatabase.trackDao().getTracksId()
        emit(tracks)
    }.flowOn(Dispatchers.IO)

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbMapper.map(track) }
    }
}
