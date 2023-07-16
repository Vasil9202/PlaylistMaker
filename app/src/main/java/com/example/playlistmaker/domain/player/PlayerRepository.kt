package com.example.playlistmaker.domain.player

interface PlayerRepository {

    fun preparePlayer(expression: String)

    fun completePlayer()

    fun getCurrentPosition() : Int

    fun release()

    fun startPlayer()

    fun pausePlayer()

    fun playBackControl()
    fun trackTimeRunnable(setTimeView: () -> Unit)

}