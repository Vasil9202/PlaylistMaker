package com.example.playlistmaker.data.playlist.impl

import com.example.playlistmaker.data.converters.PlaylistDbMapper
import com.example.playlistmaker.data.converters.PlaylistTrackDbMapper
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.domain.db.PlaylistRepository
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbMapper: PlaylistDbMapper,
    private val playlistTrackDbMapper: PlaylistTrackDbMapper
) : PlaylistRepository {
    override suspend fun savePlaylist(playlist: Playlist) {
        withContext(Dispatchers.IO){ appDatabase.playlistDao().insertPlaylist(playlistDbMapper.map(playlist))}
    }

    override suspend fun getAllPlaylists(): List<Playlist> {
       return withContext(Dispatchers.IO){ appDatabase.playlistDao().getAllPlaylist()
           .map { playlistEntity -> playlistDbMapper.map(playlistEntity) }}
    }

    override suspend fun saveTrackToPlaylistTable(track: Track) {
        withContext(Dispatchers.IO){appDatabase.playlistTrackDao().insertTrack(playlistTrackDbMapper.map(track))}
    }

    override suspend fun getPlaylistTracksByListId(tracksId: List<String>): Flow<List<Track>> = flow {
        emit(appDatabase.playlistTrackDao().getTracks()
            .filter { track -> tracksId.contains(track.trackId) }
            .map { playlistTrackEntity -> playlistTrackDbMapper.map(playlistTrackEntity) })
    }.flowOn(Dispatchers.IO)

    override suspend fun deletePlaylistTrack(track: Track) {
        withContext(Dispatchers.IO){appDatabase.playlistTrackDao().deleteTrack(playlistTrackDbMapper.map(track))}
    }
    override suspend fun deletePlaylist(playlist: Playlist) {
        withContext(Dispatchers.IO){appDatabase.playlistDao().deletePlaylist(playlistDbMapper.map(playlist))}
    }
}