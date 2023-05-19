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

        val track :Track? = intent.getParcelableExtra(TRACK)

        backButton = findViewById<ImageButton>(R.id.button_back)
        backButton.setOnClickListener {
            finish()
        }

        Glide.with(this)
            .load(track?.getCoverArtwork())
            .centerCrop()
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.DP8)))
            .placeholder(R.drawable.cover)
            .into(artworkUrl512View)

        trackNameView.text = track?.trackName
        artistNameView.text = track?.artistName
        trackTimeView.text = track?.getTrackTimeMin()
        collectionNameView.text = track?.collectionName
        releaseDateView.text = track?.getReleaseYear()
        primaryGenreNameView.text = track?.primaryGenreName
        countryView.text = track?.country
        trackCurrentTimeView.text = "00:00"


    }
}