package com.example.playlistmaker.domain.search.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMin: String,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
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
}
