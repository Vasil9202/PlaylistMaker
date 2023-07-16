package com.example.playlistmaker.ui.player.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.util.Creator

class PlayerActivityViewModel(application: Application
) : AndroidViewModel(application) {

    private val interact = Creator.providePlayerInteractor()

    private val preparePlayer = MutableLiveData(false)

    private val playerButtonIsPlay = MutableLiveData(true)

    private val currentTrackTimePosition = MutableLiveData(DEFAULT_TRACK_TIME_POSITION)

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerActivityViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
            }
        }
        private const val ONE_SECOND_IN_MILL = 1000
        private const val ONE_MINUTE_IN_SEC = 60
        private const val DEFAULT_TRACK_TIME_POSITION = "0:00"
    }

    fun getPreparePlayer(): LiveData<Boolean> = preparePlayer

     fun preparePlayer(track: Track) {
         interact.preparePlayer(track.previewUrl)
         interact.completePlayer()
         preparePlayer.postValue(true)
    }

    fun getplayerButtonIsPlay(): LiveData<Boolean> = playerButtonIsPlay

    fun playbackControl() {
        interact.playBackControl()
        if(playerButtonIsPlay.value != null){
         playerButtonIsPlay.postValue(!playerButtonIsPlay.value!!)}
    }

    fun getcurrentTrackTimePosition(): LiveData<String> = currentTrackTimePosition

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



}