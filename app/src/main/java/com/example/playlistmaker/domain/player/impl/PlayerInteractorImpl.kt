package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.PlayerRepository

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

}