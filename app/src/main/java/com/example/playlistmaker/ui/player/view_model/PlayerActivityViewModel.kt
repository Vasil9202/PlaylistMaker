package com.example.playlistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.util.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

const val DELAY_300MS = 300L
const val TIME_FORMAT = "mm:ss"
const val DEFAULT_TIME = "00:00"

class PlayerActivityViewModel(private val interact: PlayerInteractor) : ViewModel() {


    private val playerState = MutableLiveData<PlayerState>(PlayerState.Default())
    fun observePlayerState(): LiveData<PlayerState> = playerState

    private var timerJob: Job? = null

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (interact.isPlaying()) {
                delay(DELAY_300MS)
                if(interact.isPlaying()){
                playerState.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
            }}
        }
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(interact.getCurrentPosition()) ?: DEFAULT_TIME
    }

    fun initMediaPlayer(track: Track) {
        interact.initMediaPlayer(track.previewUrl){
            playerState.postValue(PlayerState.Prepared())
        }
    }

    fun onPlayButtonClicked() {
        when(playerState.value) {
            is PlayerState.Playing -> {
                pausePlayer()
            }
            is PlayerState.Prepared, is PlayerState.Paused -> {
                startPlayer()
            }
            else -> { }
        }
    }

    fun pausePlayer() {
        interact.pausePlayer()
        timerJob?.cancel()
        playerState.postValue(PlayerState.Paused(getCurrentPlayerPosition()))
    }

    private fun startPlayer() {
        interact.startPlayer()
        playerState.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
        startTimer()
    }

    fun releasePlayer() {
        interact.release()
        playerState.value = PlayerState.Default()
    }
}