package com.example.playlistmaker.data.search.impl

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.search.network.NetworkClient
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.data.search.dto.TrackSearchRequest
import com.example.playlistmaker.data.search.dto.TrackSearchResponse
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.util.Resource
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

const val TRACK_KEY = "track_key"
const val SEARCH_HISTORY = "history_search"
class TracksRepositoryImpl(private val networkClient: NetworkClient,
                           private val sharedPreferences: SharedPreferences,
                           private val gson: Gson) :
    TracksRepository {

    //private val sharedPreferences = context.getSharedPreferences(SEARCH_HISTORY, Context.MODE_PRIVATE)

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


    override fun readStorage(): List<Track> {
        val json = sharedPreferences.getString(TRACK_KEY, null) ?: return emptyList()
        return gson.fromJson(json, Array<Track>::class.java).toList()
    }

    override fun writeStorage(track: List<Track>) {
        val json = Gson().toJson(track)
        sharedPreferences.edit()
            .putString(TRACK_KEY, json)
            .apply()
    }

    override fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}
