package com.example.playlistmaker.data.shared_pref.impl

import android.content.Context
import com.example.playlistmaker.data.shared_pref.SharedPreferencesRepository
import com.example.playlistmaker.domain.model.Track
import com.google.gson.Gson

const val TRACK_KEY = "track_key"
const val SEARCH_HISTORY = "history_search"

class SharedPreferencesRepositoryImpl (context: Context) : SharedPreferencesRepository {

    private val sharedPreferences = context.getSharedPreferences(SEARCH_HISTORY, Context.MODE_PRIVATE)

    override fun readStorage(): List<Track> {
        val json = sharedPreferences.getString(TRACK_KEY, null) ?: return emptyList()
        return Gson().fromJson(json, Array<Track>::class.java).toList()
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