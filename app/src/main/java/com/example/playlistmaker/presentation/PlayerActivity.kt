package com.example.playlistmaker.presentation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.Track

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
    private lateinit var playButton: ImageButton
    private lateinit var mainThreadHandler: Handler
    private lateinit var track: Track
    private lateinit var runnable: Runnable
    private var playerState = STATE_DEFAULT
    private val interact = Creator.providePlayerInteractor()
    private var isPlaying: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        track = intent.getParcelableExtra(TRACK)!!
        viewInitialization()

        backButton.setOnClickListener {
            finish()
        }

        downloadData()

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
        interact.release()
    }

    private fun playbackControl() {

        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }


    }

    private fun preparePlayer() {
        interact.preparePlayer(track.previewUrl){
                playButton.isEnabled = true
                playerState = STATE_PREPARED
        }
        interact.preparePlayer {
                mainThreadHandler.removeCallbacks(runnable)
                trackCurrentTimeView.text = DEFAULT_TRACK_TIME_POSITION
                playButton.setImageResource(R.drawable.play);
                playerState = STATE_PREPARED
        }
        }



    private fun startPlayer() {
        isPlaying = true
        playButton.setImageResource(R.drawable.pause);
        interact.startPlayer()
        playerState = STATE_PLAYING
        mainThreadHandler.post(runnable)
    }

    private fun pausePlayer() {
        isPlaying = false
        playButton.setImageResource(R.drawable.play);
        interact.pausePlayer()
        playerState = STATE_PAUSED
    }

    private fun viewInitialization(){
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
        backButton = findViewById(R.id.button_back)
        runnable = Runnable {
            if (isPlaying) {
                val seconds = interact.getCurrentPosition() / ONE_SECOND_IN_MILL
                trackCurrentTimeView.text = String.format("%d:%02d", seconds / ONE_MINUTE_IN_SEC, seconds % ONE_MINUTE_IN_SEC)
                mainThreadHandler.postDelayed(runnable, DELAY)
            }
        }
    }

    private fun downloadData(){
        Glide.with(this)
            .load(track.getCoverArtwork())
            .centerCrop()
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.DP8)))
            .placeholder(R.drawable.cover)
            .into(artworkUrl512View)

        trackNameView.text = track.trackName
        artistNameView.text = track.artistName
        trackTimeView.text = track.getTrackTimeMin()
        collectionNameView.text = track.collectionName
        releaseDateView.text = track.getReleaseYear()
        primaryGenreNameView.text = track.primaryGenreName
        countryView.text = track.country
        trackCurrentTimeView.text = DEFAULT_TRACK_TIME_POSITION
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 300L
        private const val DEFAULT_TRACK_TIME_POSITION = "0:00"
        private const val ONE_SECOND_IN_MILL = 1000
        private const val ONE_MINUTE_IN_SEC = 60
    }
}