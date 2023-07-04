package com.example.playlistmaker.data

import android.content.SharedPreferences
import com.example.playlistmaker.domain.model.Track

interface TrackStorage {

    fun write(sharedPreferences: SharedPreferences, track: Array<Track>)

    fun read(sharedPreferences: SharedPreferences): Array<Track>
}