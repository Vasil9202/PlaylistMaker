package com.example.playlistmaker.domain.player

import com.example.playlistmaker.domain.model.Track

interface PlayerInteractor {

    fun initMediaPlayer(trackURL: String, playerState: () -> Unit)

    fun startPlayer()

    fun isPlaying() : Boolean

    fun pausePlayer()

    fun getCurrentPosition() : Int

    fun release()

    suspend fun addTrackToFavourite(track: Track)

    suspend fun deleteTrackFromFavourite(track: Track)
}