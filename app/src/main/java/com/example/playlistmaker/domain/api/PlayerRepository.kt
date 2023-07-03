package com.example.playlistmaker.domain.api

import android.media.MediaPlayer

interface PlayerRepository {

    fun preparePlayer(expression: String, onPreparedListener: () -> Unit)

    fun preparePlayer(onCompletionListener: () -> Unit)

    fun getCurrentPosition() : Int

    fun release()

    fun startPlayer()

    fun pausePlayer()

}