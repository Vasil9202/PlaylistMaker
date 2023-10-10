package com.example.playlistmaker.data.favourite_tracks.impl

import com.example.playlistmaker.data.converters.FavouriteTrackDbMapper
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.entity.FavouriteTrackEntity
import com.example.playlistmaker.domain.db.FavouriteTrackRepository
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FavouriteTrackRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val favouriteTrackDbMapper: FavouriteTrackDbMapper
) : FavouriteTrackRepository {

    override fun getfavouriteTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.favouriteTrackDao().getTracks()
        emit(convertFromTrackEntity(tracks))
    }.flowOn(Dispatchers.IO)

    override fun getfavouriteTracksId(): Flow<List<String>> = flow {
        val tracks = appDatabase.favouriteTrackDao().getTracksId()
        emit(tracks)
    }.flowOn(Dispatchers.IO)

    private fun convertFromTrackEntity(tracks: List<FavouriteTrackEntity>): List<Track> {
        return tracks.map { track -> favouriteTrackDbMapper.map(track) }
    }
}
