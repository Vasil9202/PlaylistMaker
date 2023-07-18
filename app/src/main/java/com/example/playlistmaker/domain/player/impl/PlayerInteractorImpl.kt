package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.PlayerRepository

class PlayerInteractorImpl (private val repository: PlayerRepository) : PlayerInteractor {

    override fun preparePlayer(trackURL: String){
        repository.preparePlayer(trackURL)
    }

    override fun completePlayer() {
        repository.completePlayer()
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

    override fun playBackControl() {
        repository.playBackControl()
    }

    override fun trackTimeRunnable(setTimeView: () -> Unit) {
        repository.trackTimeRunnable(setTimeView)
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