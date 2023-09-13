package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.R
import com.example.playlistmaker.data.converters.TrackDbMapper
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.search.dto.TrackSearchRequest
import com.example.playlistmaker.data.search.dto.TrackSearchResponse
import com.example.playlistmaker.data.search.network.NetworkClient
import com.example.playlistmaker.data.search.storage.TrackStorage
import com.example.playlistmaker.data.search.storage.model.TracksList
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext


class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val storage: TrackStorage,
    private val appDatabase: AppDatabase,
    private val trackDbMapper: TrackDbMapper
) :
    TracksRepository {


    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error(R.string.net_error.toString()))
            }

            200 -> {
                with(response as TrackSearchResponse) {
                    val data = results.map {
                        Track(
                            it.trackId,
                            it.trackName,
                            it.artistName,
                            it.trackTimeMin(),
                            it.artworkUrl100,
                            it.collectionName,
                            it.releaseDate.orEmpty(),
                            it.primaryGenreName,
                            it.country,
                            it.previewUrl
                        )
                    }
                    isTracksFavourite(data)
                    emit(Resource.Success(data))
                }
            }

            -2 -> {
                emit(Resource.Error(R.string.find_nothing.toString()))
            }

            else -> {
                emit(Resource.Error(R.string.net_error.toString()))
            }
        }
    }.flowOn(Dispatchers.IO)


    override fun readStorage(): List<Track> {
        return storage.readStorage().map {
            Track(
                it.trackId,
                it.trackName,
                it.artistName,
                it.trackTimeMin,
                it.artworkUrl100,
                it.collectionName,
                it.releaseDate,
                it.primaryGenreName,
                it.country,
                it.previewUrl
            )
        }
    }

    override fun writeStorage(track: List<Track>) {
        storage.writeStorage(track.map {
            TracksList(
                it.trackId,
                it.trackName,
                it.artistName,
                it.trackTimeMin,
                it.artworkUrl100,
                it.collectionName,
                it.releaseDate,
                it.primaryGenreName,
                it.country,
                it.previewUrl
            )
        })
    }

    override fun clear() {
        storage.clear()
    }

    override suspend fun isTracksFavourite(list: List<Track>) {
        withContext(Dispatchers.IO){ list.map { obj ->
            obj.isFavorite = appDatabase.trackDao().getTracksId().contains(obj.trackId)}
        }
    }

    override suspend fun getFavouriteTracks(): List<Track> {
        return withContext(Dispatchers.IO){ appDatabase.trackDao().getTracks().map { trackDbMapper.map(it) }}
    }
}
