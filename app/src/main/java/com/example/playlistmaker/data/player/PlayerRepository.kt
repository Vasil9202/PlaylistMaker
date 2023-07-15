package com.example.playlistmaker.data.player

import android.media.MediaPlayer

interface PlayerRepository {

    fun preparePlayer(expression: String)

    fun preparePlayer()

    fun getCurrentPosition() : Int

    fun release()

    fun startPlayer()

    fun pausePlayer()

    fun playBackControl()
    fun trackTimeRunnable(setTimeView: () -> Unit)

}