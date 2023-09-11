package com.example.playlistmaker.domain.player

import com.example.playlistmaker.domain.model.Track

interface PlayerRepository {

    fun initMediaPlayer(expression: String, playerState: () -> Unit)

    fun getCurrentPosition() : Int

    fun isPlaying() : Boolean

    fun release()

    fun startPlayer()

    fun pausePlayer()

   suspend fun addTrackToFavourite(track: Track)

   suspend fun deleteTrackFromFavourite(track: Track)




}