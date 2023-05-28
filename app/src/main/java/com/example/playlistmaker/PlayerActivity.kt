package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
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
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 300L
        private const val DEFAULT_TRACK_TIME_POSITION = "0:00"
    }

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
    private lateinit var playButton: ImageButton
    private lateinit var previewURL: String
    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()
    private var mainThreadHandler: Handler? = null
    private var isPlaying: Boolean = false
    private lateinit var runnable: Runnable

    @SuppressLint("MissingInflatedId", "ResourceType")
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
        playButton = findViewById(R.id.play_button)
        mainThreadHandler = Handler(Looper.getMainLooper())


        val track :Track? = intent.getParcelableExtra(TRACK)
        if (track != null) {
            previewURL = track.previewUrl
        }

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
        trackCurrentTimeView.text = DEFAULT_TRACK_TIME_POSITION
        runnable = Runnable {
            if (isPlaying) {
                val seconds = mediaPlayer.currentPosition / 1000
                trackCurrentTimeView.text = String.format("%d:%02d", seconds / 60, seconds % 60)
                mainThreadHandler?.postDelayed(runnable, DELAY)
            }
        }

        preparePlayer()
        playButton.setOnClickListener {
            playbackControl()
        }
    }
    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(previewURL)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            mainThreadHandler?.removeCallbacks(runnable)
            trackCurrentTimeView.text = DEFAULT_TRACK_TIME_POSITION
            playButton.setImageResource(R.drawable.play);
            playerState = STATE_PREPARED
        }
    }

    private fun startPlayer() {
        isPlaying = true
        playButton.setImageResource(R.drawable.pause);
        mediaPlayer.start()
        playerState = STATE_PLAYING
        mainThreadHandler?.post(runnable)
    }

    private fun pausePlayer() {
        isPlaying = false
        playButton.setImageResource(R.drawable.play);
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }
}