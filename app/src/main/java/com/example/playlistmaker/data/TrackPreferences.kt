package com.example.playlistmaker.data

import android.content.SharedPreferences
import com.example.playlistmaker.domain.model.Track
import com.google.gson.Gson

const val TRACK_KEY = "track_key"

class TrackPreferences() : TrackStorage {

    override fun read(sharedPreferences: SharedPreferences): Array<Track> {
        val json = sharedPreferences.getString(TRACK_KEY, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    // запись
    override fun write(sharedPreferences: SharedPreferences, track: Array<Track>) {
        val json = Gson().toJson(track)
        sharedPreferences.edit()
            .putString(TRACK_KEY, json)
            .apply()
    }
}