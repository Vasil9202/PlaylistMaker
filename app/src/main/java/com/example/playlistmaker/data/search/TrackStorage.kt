package com.example.playlistmaker.data.search

import android.content.SharedPreferences
import com.example.playlistmaker.domain.search.model.Track

interface TrackStorage {

    fun write(sharedPreferences: SharedPreferences, track: Array<Track>)

    fun read(sharedPreferences: SharedPreferences): Array<Track>
}