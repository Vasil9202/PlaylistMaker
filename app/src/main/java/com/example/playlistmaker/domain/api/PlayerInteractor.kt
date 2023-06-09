package com.example.playlistmaker.domain.api

interface PlayerInteractor {

    fun preparePlayer(trackURL: String, onPreparedListener: () -> Unit)

    fun preparePlayer(onCompletionListener: () -> Unit)

    fun startPlayer()

    fun pausePlayer()

    fun getCurrentPosition() : Int

    fun release()

    fun playBackControl(setPlayButton:Pair<() -> Unit, () -> Unit>)

    fun trackTimeRunnable(setTimeView: () -> Unit)



}