package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.PlayerRepository
import kotlinx.coroutines.flow.Flow

class PlayerInteractorImpl (private val repository: PlayerRepository) : PlayerInteractor {

    override fun initMediaPlayer(trackURL: String, playerState: () -> Unit){
        repository.initMediaPlayer(trackURL,playerState)
    }

    override fun startPlayer() {
        repository.startPlayer()
    }

    override fun pausePlayer() {
        repository.pausePlayer()
    }

    override fun getCurrentPosition(): Int {
        return repository.getCurrentPosition()
    }



    override fun release() {
        repository.release()
    }

    override fun isPlaying(): Boolean {
        return repository.isPlaying()
    }

    override suspend fun addTrackToFavourite(track: Track) {
        repository.addTrackToFavourite(track)
    }

    override suspend fun deleteTrackFromFavourite(track: Track) {
        repository.deleteTrackFromFavourite(track)
    }


}