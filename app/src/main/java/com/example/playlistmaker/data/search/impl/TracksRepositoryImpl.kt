package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.data.search.network.NetworkClient
import com.example.playlistmaker.data.search.TracksRepository
import com.example.playlistmaker.data.search.dto.TrackSearchRequest
import com.example.playlistmaker.data.search.dto.TrackSearchResponse
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.util.Resource
import java.text.SimpleDateFormat
import java.util.Locale

class TracksRepositoryImpl(private val networkClient: NetworkClient) :
    TracksRepository {

    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }
            200 -> {
                Resource.Success((response as TrackSearchResponse).results.map {
                    Track(
                        it.trackName,
                        it.artistName,
                        SimpleDateFormat("mm:ss", Locale.getDefault()).format(it.trackTimeMillis.toLong()),
                        it.artworkUrl100,
                        it.collectionName,
                        it.releaseDate,
                        it.primaryGenreName,
                        it.country,
                        it.previewUrl
                    )            })
            }
            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }
}