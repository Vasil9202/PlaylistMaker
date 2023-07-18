package com.example.playlistmaker.domain.player

interface PlayerInteractor {

    fun preparePlayer(trackURL: String)

    fun completePlayer()

    fun startPlayer()

    fun pausePlayer()

    fun getCurrentPosition() : Int

    fun release()

    fun playBackControl()

    fun trackTimeRunnable(setTimeView: () -> Unit)



}