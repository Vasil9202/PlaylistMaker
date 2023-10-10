package com.example.playlistmaker.ui.media_lib.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.search.storage.model.TracksList
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PlaylistViewModel(
private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val playlists = MutableLiveData<List<Playlist>> (emptyList())

    private val tracksList = MutableLiveData<List<Track>> (emptyList())
    private val playlistTracks :MutableList<Track> = mutableListOf()
    fun playlistsStatus() :LiveData<List<Playlist>> = playlists
    fun tracksListStatus() :LiveData<List<Track>> = tracksList

    fun getAllPlaylists(){
        viewModelScope.launch {
                playlists.postValue(playlistInteractor.getAllPlaylists())
        }
    }

    fun savePlaylist(playlist: Playlist){
        viewModelScope.launch {
            playlistInteractor.savePlaylist(playlist)
        }
    }

    fun getTracksById(tracks: List<String>){
        viewModelScope.launch {
            val tracks = playlistInteractor.getPlaylistTracksByListId(tracks).first()
            playlistTracks.clear()
            playlistTracks.addAll(tracks)
            tracksList.postValue(tracks)
        }
    }

    fun deleteTrack(playlist: Playlist, track: Track){
        playlist.tracksIdList.remove(track.trackId)
        playlist.tracksCount = playlist.tracksIdList.size
        viewModelScope.launch { playlistInteractor.savePlaylist(playlist) }
        playlistTracks.remove(track)
        tracksList.postValue(playlistTracks)
    }

    fun deletePlaylist(playlist: Playlist){
        viewModelScope.launch { playlistInteractor.deletePlaylist(playlist) }
    }

    fun editPlaylist(playlist: Playlist){
        viewModelScope.launch {
            playlistInteractor.savePlaylist(playlist)
        }
    }
}