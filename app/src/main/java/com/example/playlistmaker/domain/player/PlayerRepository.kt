package com.example.playlistmaker.domain.player

interface PlayerRepository {

    fun initMediaPlayer(expression: String, playerState: () -> Unit)

    fun getCurrentPosition() : Int

    fun isPlaying() : Boolean

    fun release()

    fun startPlayer()

    fun pausePlayer()


}