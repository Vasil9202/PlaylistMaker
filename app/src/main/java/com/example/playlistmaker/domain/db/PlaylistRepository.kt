package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track

interface  PlaylistRepository {

    suspend fun savePlaylist(playlist: Playlist)
    suspend fun getAllPlaylists(): List<Playlist>
    suspend fun saveTrackToPlaylistTable(track: Track)

}