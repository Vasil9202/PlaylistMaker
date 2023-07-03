package com.example.playlistmaker.domain.impl

import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.PlayerRepository
import java.util.concurrent.Executors

class PlayerInteractorImpl (private val repository: PlayerRepository) : PlayerInteractor {

    override fun preparePlayer(trackURL: String, onPreparedListener : () -> Unit){
        repository.preparePlayer(trackURL,onPreparedListener)
    }

    override fun preparePlayer(onCompletionListener: () -> Unit) {
        repository.preparePlayer(onCompletionListener)
    }

    override fun startPlayer() {
        repository.startPlayer()
    }

    override fun pausePlayer() {
        repository.pausePlayer()
    }

    override fun getCurrentPosition(): Int {
        return repository.getCurrentPosition()
    }

    override fun release() {
        repository.release()
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
        private var PLAYER_STATE = STATE_DEFAULT
        private var PLAYER_PLAY = false
    }

}