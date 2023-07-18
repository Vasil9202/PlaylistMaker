package com.example.playlistmaker.data.player.impl

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.domain.player.PlayerRepository


class PlayerRepositoryImpl() : PlayerRepository {

    private var playerState = STATE_DEFAULT
    private var isPlaying: Boolean = false
    private val mediaPlayer = MediaPlayer()
    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private lateinit var runnable : Runnable



    override fun preparePlayer(expression: String) {
        mediaPlayer.setDataSource(expression)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener  {
            playerState = STATE_PREPARED
        }
    }

    override fun completePlayer() {
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            mainThreadHandler.removeCallbacks(runnable)
        }
    }

    override fun startPlayer() {
        isPlaying = true
        playerState = STATE_PLAYING
        mediaPlayer.start()
        mainThreadHandler.post(runnable)

    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        isPlaying = false
        playerState = STATE_PAUSED
    }

    override fun playBackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun trackTimeRunnable(setTimeView: () -> Unit) {
        runnable = Runnable {
            if (isPlaying) {
                setTimeView()
                mainThreadHandler.postDelayed(runnable, DELAY)
            }
        }
    }

    override fun getCurrentPosition() : Int{
        return mediaPlayer.currentPosition
    }

    override fun release() {
        mediaPlayer.release()
    }

    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
        const val DELAY = 300L
        const val DEFAULT_TRACK_TIME_POSITION = "0:00"
        const val ONE_SECOND_IN_MILL = 1000
        const val ONE_MINUTE_IN_SEC = 60
    }


}