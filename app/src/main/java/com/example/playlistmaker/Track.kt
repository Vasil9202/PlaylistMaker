package com.example.playlistmaker

import android.content.SharedPreferences
import android.os.Parcelable
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

const val TRACK_KEY = "track_key"
@Parcelize
data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
) : Parcelable{

    fun getTrackTimeMin() = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis.toLong())
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
    //Release date in iTunes in “yyyy MM dd” format
    fun getReleaseYear(): String{
        try {
          return SimpleDateFormat("yyyy", Locale.getDefault()).
            format(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(releaseDate))
        }catch (e: Exception){
            return ""
        } }
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