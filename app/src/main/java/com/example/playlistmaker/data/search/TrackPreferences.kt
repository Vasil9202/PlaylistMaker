package com.example.playlistmaker.data.search

import android.content.SharedPreferences
import com.example.playlistmaker.domain.search.model.Track
import com.google.gson.Gson

const val TRACK_KEY = "track_key"

class TrackPreferences private constructor() : TrackStorage {
    companion object {

        @Volatile
        private var instance: TrackPreferences? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: TrackPreferences().also { instance = it }
            }
    }

    override fun read(sharedPreferences: SharedPreferences): Array<Track> {
        val json = sharedPreferences.getString(TRACK_KEY, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    override fun write(sharedPreferences: SharedPreferences, track: Array<Track>) {
        val json = Gson().toJson(track)
        sharedPreferences.edit()
            .putString(TRACK_KEY, json)
            .apply()
    }

    fun clear(sharedPreferences: SharedPreferences) {
        sharedPreferences.edit().clear().apply()
    }
}