package com.example.playlistmaker.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class Track(
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMilliSec: String,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    var isFavorite: Boolean = false
) : Parcelable{

    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
    //Release date in iTunes in “yyyy MM dd” format
    fun getReleaseYear(): String{
        try {
          return SimpleDateFormat("yyyy", Locale.getDefault()).
            format(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(releaseDate))
        }catch (e: Exception){
            return ""
        } }

    fun trackTimeMinute() : String{
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMilliSec.toLong())
    }
}
