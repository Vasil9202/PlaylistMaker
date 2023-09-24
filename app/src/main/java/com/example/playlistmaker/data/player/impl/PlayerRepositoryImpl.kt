package com.example.playlistmaker.data.player.impl

import android.media.MediaPlayer
import com.example.playlistmaker.data.converters.TrackDbMapper
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.player.PlayerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class PlayerRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbMapper: TrackDbMapper
) : PlayerRepository {

    private var mediaPlayer = MediaPlayer()

    override fun initMediaPlayer(expression: String, playerState: () -> Unit) {
        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(expression)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState()
        }
        mediaPlayer.setOnCompletionListener {
            playerState()
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun release() {
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    override suspend fun addTrackToFavourite(track: Track) {
        withContext(Dispatchers.IO){appDatabase.trackDao().insertTrack(trackDbMapper.map(track))}
    }

    override suspend fun deleteTrackFromFavourite(track: Track) {
        withContext(Dispatchers.IO){appDatabase.trackDao().deleteTrack(trackDbMapper.map(track))}
    }
}