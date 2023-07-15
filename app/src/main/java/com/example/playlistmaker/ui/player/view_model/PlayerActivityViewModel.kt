package com.example.playlistmaker.ui.player.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.util.Creator

class PlayerActivityViewModel(application: Application
) : AndroidViewModel(application) {

    private val interact = Creator.providePlayerInteractor()

    val preparePlayer: MutableLiveData<Boolean> = MutableLiveData(false)

    val playerButtonIsPlay: MutableLiveData<Boolean> = MutableLiveData(true)

    val currentTrackTimePosition: MutableLiveData<String> = MutableLiveData()

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerActivityViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
            }
        }
        private const val ONE_SECOND_IN_MILL = 1000
        private const val ONE_MINUTE_IN_SEC = 60
    }


     fun preparePlayer(track: Track) {
         interact.preparePlayer(track.previewUrl)
         interact.preparePlayer()
         preparePlayer.postValue(true)
    }

     fun playbackControl() {
        interact.playBackControl()
         playerButtonIsPlay.postValue(!playerButtonIsPlay.value!!)
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



}