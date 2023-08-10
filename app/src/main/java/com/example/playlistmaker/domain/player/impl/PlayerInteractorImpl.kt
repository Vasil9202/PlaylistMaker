package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.PlayerRepository

class PlayerInteractorImpl (private val repository: PlayerRepository) : PlayerInteractor {

    override fun preparePlayer(trackURL: String){
        repository.preparePlayer(trackURL)
    }

    override fun completePlayer(changeViewButton: () -> Unit)  {
        repository.completePlayer(changeViewButton)
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

    override fun playBackControl()  : Int{
        return repository.playBackControl()
    }

    override fun trackTimeRunnable(setTimeView: () -> Unit) {
        repository.trackTimeRunnable(setTimeView)
    }

}