package com.example.playlistmaker.ui.player.view_model

import android.util.Log
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



class PlayerActivityViewModel(private val interact: PlayerInteractor) : ViewModel() {


    private val playerState = MutableLiveData<PlayerState>(PlayerState.Default())

    private val isFavorite  = MutableLiveData<Boolean>(false)
    fun observePlayerState(): LiveData<PlayerState> = playerState
    fun onFavoriteState(): LiveData<Boolean> = isFavorite

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
        isFavorite.postValue(track.isFavorite)
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

    fun onFavoriteClicked(track: Track){
        viewModelScope.launch {
        if(track.isFavorite){
            track.isFavorite = false
            interact.deleteTrackFromFavourite(track)
            isFavorite.postValue(false)
            Log.i("facClick","172")

        }
        else{
            track.isFavorite = true
            interact.addTrackToFavourite(track)
            isFavorite.postValue(true)
            Log.i("facClick","112")
        }}
    }
    companion object{
        private const val DELAY_300MS = 300L
        private const val TIME_FORMAT = "mm:ss"
        private const val DEFAULT_TIME = "00:00"
    }
}