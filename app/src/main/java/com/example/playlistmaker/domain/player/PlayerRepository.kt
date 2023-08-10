package com.example.playlistmaker.domain.player

interface PlayerRepository {

    fun preparePlayer(expression: String)

    fun completePlayer(changeViewButton: () -> Unit)

    fun getCurrentPosition() : Int

    fun release()

    fun startPlayer()

    fun pausePlayer()

    fun playBackControl() : Int
    fun trackTimeRunnable(setTimeView: () -> Unit)

}