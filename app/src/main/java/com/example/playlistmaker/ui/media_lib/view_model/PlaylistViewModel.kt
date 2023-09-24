package com.example.playlistmaker.ui.media_lib.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.model.Playlist
import kotlinx.coroutines.launch

class PlaylistViewModel(
private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val playlists = MutableLiveData<List<Playlist>> (emptyList())
    fun playlistsStatus() :LiveData<List<Playlist>> = playlists

    fun getAllPlaylists(){
        viewModelScope.launch {
            val list = playlistInteractor.getAllPlaylists()
            if (list.isNotEmpty()) {
                playlists.postValue(playlistInteractor.getAllPlaylists())
            }
        }
    }

    fun savePlaylist(playlist: Playlist){
        viewModelScope.launch {
            playlistInteractor.savePlaylist(playlist)
        }
    }
}