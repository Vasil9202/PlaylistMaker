package com.example.playlistmaker.domain.player

interface PlayerInteractor {

    fun initMediaPlayer(trackURL: String, playerState: () -> Unit)

    fun startPlayer()

    fun isPlaying() : Boolean

    fun pausePlayer()

    fun getCurrentPosition() : Int

    fun release()

}