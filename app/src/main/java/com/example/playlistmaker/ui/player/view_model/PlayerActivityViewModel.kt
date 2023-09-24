package com.example.playlistmaker.ui.player.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.util.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Locale



class PlayerActivityViewModel(
    private val playerInteractor: PlayerInteractor,
    private val playlistInteractor: PlaylistInteractor
    ) : ViewModel() {


    private val playerState = MutableLiveData<PlayerState>(PlayerState.Default())

    private val playlistState  = MutableLiveData<List<Playlist>>()

    private val isFavorite  = MutableLiveData<Boolean>(false)
    fun observePlayerState(): LiveData<PlayerState> = playerState
    fun onFavoriteState(): LiveData<Boolean> = isFavorite

    fun observePlaylistState(): LiveData<List<Playlist>> = playlistState

    private var timerJob: Job? = null

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (playerInteractor.isPlaying()) {
                delay(DELAY_300MS)
                if(playerInteractor.isPlaying()){
                playerState.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
            }}
        }
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(playerInteractor.getCurrentPosition()) ?: DEFAULT_TIME
    }

    fun initMediaPlayer(track: Track) {
        isFavorite.postValue(track.isFavorite)
        playerInteractor.initMediaPlayer(track.previewUrl){
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
        playerInteractor.pausePlayer()
        timerJob?.cancel()
        playerState.postValue(PlayerState.Paused(getCurrentPlayerPosition()))
    }

    private fun startPlayer() {
        playerInteractor.startPlayer()
        playerState.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
        startTimer()
    }

    fun releasePlayer() {
        playerInteractor.release()
        playerState.value = PlayerState.Default()
    }

    fun getPlaylists(){
        viewModelScope.launch {
            playlistState.postValue(playlistInteractor.getAllPlaylists()) }
    }

    fun onFavoriteClicked(track: Track){
        viewModelScope.launch {
        if(track.isFavorite){
            track.isFavorite = false
            playerInteractor.deleteTrackFromFavourite(track)
            isFavorite.postValue(false)
        }
        else{
            track.isFavorite = true
            playerInteractor.addTrackToFavourite(track)
            isFavorite.postValue(true)
        }}
    }

    fun addTrackToPlaylist(playlist: Playlist, track: Track){
        playlist.tracksIdList.add(track.trackId)
        playlist.tracksCount = playlist.tracksCount + 1
        viewModelScope.launch {
            playlistInteractor.savePlaylist(playlist)
            playlistInteractor.saveTrackToPlaylistTable(track)
            playlistState.postValue(playlistInteractor.getAllPlaylists())
        }
    }
    companion object{
        private const val DELAY_300MS = 300L
        private const val TIME_FORMAT = "mm:ss"
        private const val DEFAULT_TIME = "00:00"
    }
}