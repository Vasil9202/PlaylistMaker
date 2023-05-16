package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var artworkUrl512View: ImageView
    private lateinit var trackNameView: TextView
    private lateinit var artistNameView: TextView
    private lateinit var trackTimeView: TextView
    private lateinit var trackCurrentTimeView: TextView
    private lateinit var collectionNameView: TextView
    private lateinit var releaseDateView: TextView
    private lateinit var primaryGenreNameView: TextView
    private lateinit var countryView: TextView
    private lateinit var backButton: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)


        artworkUrl512View = findViewById(R.id.placeholder)
        trackNameView = findViewById(R.id.track_name)
        artistNameView = findViewById(R.id.group)
        trackCurrentTimeView = findViewById(R.id.current_time)
        trackTimeView = findViewById(R.id.track_time)
        collectionNameView = findViewById(R.id.track_album)
        releaseDateView = findViewById(R.id.track_year)
        primaryGenreNameView = findViewById(R.id.track_genre)
        countryView = findViewById(R.id.track_country)

        val trackName = intent.getStringExtra("trackName")
        val artistName = intent.getStringExtra("artistName")
        val trackTime = intent.getStringExtra("trackTime")
        val coverArtwork = intent.getStringExtra("coverArtwork")
        val collectionName = intent.getStringExtra("collectionName")
        val releaseDate = intent.getStringExtra("releaseDate")
        val primaryGenreName = intent.getStringExtra("primaryGenreName")
        val country = intent.getStringExtra("country")

        backButton = findViewById<ImageButton>(R.id.button_back)
        backButton.setOnClickListener {
            finish()
        }

        Glide.with(this)
            .load(coverArtwork)
            .centerCrop()
            .transform(RoundedCorners(artworkUrl512View.resources.getDimensionPixelSize(R.dimen.DP8)))
            .placeholder(R.drawable.cover)
            .into(artworkUrl512View)

        trackNameView.text = trackName
        artistNameView.text = artistName
        trackTimeView.text = trackTime
        collectionNameView.text = collectionName
        releaseDateView.text = releaseDate?.substring(0,4)
        primaryGenreNameView.text = primaryGenreName
        countryView.text = country
        trackCurrentTimeView.text = "00:00"


    }
}