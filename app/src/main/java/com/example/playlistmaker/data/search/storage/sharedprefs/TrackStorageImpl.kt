package com.example.playlistmaker.data.search.storage.sharedprefs

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.search.storage.TrackStorage
import com.example.playlistmaker.data.search.storage.model.TracksList
import com.example.playlistmaker.domain.model.Track
import com.google.gson.Gson


const val TRACK_KEY = "track_key"
const val SEARCH_HISTORY = "history_search"
class TrackStorageImpl(private val context: Context,private val gson: Gson) : TrackStorage{

   private val sharedPreferences = context.getSharedPreferences(SEARCH_HISTORY, Context.MODE_PRIVATE)

    override fun readStorage(): List<TracksList> {
        val json = sharedPreferences.getString(TRACK_KEY, null) ?: return emptyList()
        return gson.fromJson(json, Array<TracksList>::class.java).toList()
    }

    override fun writeStorage(track: List<TracksList>) {
        val json = gson.toJson(track)
        sharedPreferences.edit()
            .putString(TRACK_KEY, json)
            .apply()
    }

    override fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}