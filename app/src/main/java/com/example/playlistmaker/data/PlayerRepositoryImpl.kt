package com.example.playlistmaker.data

import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.PlayerRepository

class PlayerRepositoryImpl() : PlayerRepository {

    private val mediaPlayer = MediaPlayer()

    override fun preparePlayer(expression: String, onPreparedListener: () -> Unit) {
        mediaPlayer.setDataSource(expression)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener  { onPreparedListener()  }
    }

    override fun preparePlayer(onCompletionListener: () -> Unit) {
        mediaPlayer.setOnCompletionListener { onCompletionListener() }
    }

    override fun startPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
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