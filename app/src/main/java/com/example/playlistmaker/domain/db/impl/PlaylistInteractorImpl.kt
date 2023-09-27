package com.example.playlistmaker.domain.db.impl

import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.db.PlaylistRepository
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl (
    private val playlistRepository: PlaylistRepository
) : PlaylistInteractor{
    override suspend fun savePlaylist(playlist: Playlist) {
        playlistRepository.savePlaylist(playlist)
    }

    override suspend fun getAllPlaylists(): List<Playlist> {
       return playlistRepository.getAllPlaylists()
    }

    override suspend fun saveTrackToPlaylistTable(track: Track) {
        playlistRepository.saveTrackToPlaylistTable(track)
    }



}