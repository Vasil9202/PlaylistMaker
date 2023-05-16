package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

const val TRACK_KEY = "track_key"

data class Track(val trackName: String, val artistName: String, val trackTimeMillis: String, val artworkUrl100: String,
                 val collectionName: String, val releaseDate: String, val primaryGenreName: String, val country: String){

    fun getTrackTimeMin() = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis.toLong())
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")

}

class TrackPreferences {

    fun read(sharedPreferences: SharedPreferences): Array<Track> {
        val json = sharedPreferences.getString(TRACK_KEY, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    // запись
    fun write(sharedPreferences: SharedPreferences, track: Array<Track>) {
        val json = Gson().toJson(track)
        sharedPreferences.edit()
            .putString(TRACK_KEY, json)
            .apply()
    }


}