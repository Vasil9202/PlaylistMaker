package com.example.playlistmaker.data.shared_pref

import com.example.playlistmaker.domain.model.Track

interface SharedPreferencesRepository {

    fun writeStorage(track: List<Track>)

    fun readStorage(): List<Track>

    fun clear()
}

