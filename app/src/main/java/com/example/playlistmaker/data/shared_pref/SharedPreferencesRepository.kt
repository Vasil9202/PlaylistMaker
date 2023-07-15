package com.example.playlistmaker.data.shared_pref

import android.content.SharedPreferences
import com.example.playlistmaker.domain.model.Track

interface SharedPreferencesRepository {

    fun write(sharedPreferences: SharedPreferences, track: Array<Track>)

    fun read(sharedPreferences: SharedPreferences): Array<Track>
}