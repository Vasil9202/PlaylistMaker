package com.example.playlistmaker.data.shared_pref

import android.content.SharedPreferences
import com.example.playlistmaker.domain.model.Track

interface SharedPreferencesRepository {

    fun write( track: List<Track>)

    fun read(): List<Track>

    fun clear()
}

