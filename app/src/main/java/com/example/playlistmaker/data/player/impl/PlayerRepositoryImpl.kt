package com.example.playlistmaker.data.player.impl

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.domain.player.PlayerRepository


class PlayerRepositoryImpl : PlayerRepository {

    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()
    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private lateinit var runnable : Runnable



    override fun preparePlayer(expression: String) {
        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(expression)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener  {
            playerState = STATE_PREPARED
        }
    }

    override fun completePlayer(changeViewButton: () -> Unit)  {
        mediaPlayer.setOnCompletionListener {
            changeViewButton()
            playerState = STATE_PREPARED
            mainThreadHandler.removeCallbacks(runnable)
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        mainThreadHandler.post(runnable)

    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }

    override fun playBackControl() : Int{
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
        return playerState
    }

    override fun trackTimeRunnable(setTimeView: () -> Unit) {
        runnable = Runnable {
            if (playerState == STATE_PLAYING) {
                mainThreadHandler.postDelayed(runnable, DELAY)
                setTimeView()
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
    }


}