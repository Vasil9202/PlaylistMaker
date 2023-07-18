package com.example.playlistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.util.Creator

class PlayerActivityViewModel(private val interact: PlayerInteractor) : ViewModel() {


    private val preparePlayer = MutableLiveData(false)

    private val playerButtonIsPlay = MutableLiveData(true)

    private val currentTrackTimePosition = MutableLiveData(DEFAULT_TRACK_TIME_POSITION)

    fun getPreparePlayer(): LiveData<Boolean> = preparePlayer
    fun getPlayerButtonIsPlay(): LiveData<Boolean> = playerButtonIsPlay
    fun getCurrentTrackTimePosition(): LiveData<String> = currentTrackTimePosition

    fun preparePlayer(track: Track) {
         interact.preparePlayer(track.previewUrl)
         interact.completePlayer()
         preparePlayer.postValue(true)
    }

    fun playbackControl() {
        interact.playBackControl()
        if(playerButtonIsPlay.value != null){
         playerButtonIsPlay.postValue(!playerButtonIsPlay.value!!)}
    }

    fun setTrackTimeRunnable(){
        interact.trackTimeRunnable {
            val seconds = interact.getCurrentPosition() / ONE_SECOND_IN_MILL
            currentTrackTimePosition.postValue(String.format("%d:%02d", seconds / ONE_MINUTE_IN_SEC, seconds % ONE_MINUTE_IN_SEC))
        }
    }

    fun pausePlayer(){
        interact.pausePlayer()
    }

    fun releasePlayer(){
        interact.release()
    }
    companion object {
        private const val ONE_SECOND_IN_MILL = 1000
        private const val ONE_MINUTE_IN_SEC = 60
        private const val DEFAULT_TRACK_TIME_POSITION = "0:00"
    }
}