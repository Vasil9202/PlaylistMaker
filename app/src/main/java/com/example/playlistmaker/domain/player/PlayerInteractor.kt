package com.example.playlistmaker.domain.player

interface PlayerInteractor {

    fun preparePlayer(trackURL: String)

    fun completePlayer(changeViewButton: () -> Unit)

    fun startPlayer()

    fun pausePlayer()

    fun getCurrentPosition() : Int

    fun release()

    fun playBackControl()  : Int

    fun trackTimeRunnable(setTimeView: () -> Unit)



}