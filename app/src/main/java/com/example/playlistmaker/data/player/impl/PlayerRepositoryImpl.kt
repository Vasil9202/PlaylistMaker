package com.example.playlistmaker.data.player.impl

import android.media.MediaPlayer
import com.example.playlistmaker.data.converters.TrackDbConvertor
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.player.PlayerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class PlayerRepositoryImpl(private val appDatabase: AppDatabase,
                           private val trackDbConvertor: TrackDbConvertor) : PlayerRepository {

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
       appDatabase.trackDao().insertTrack(trackDbConvertor.map(track))
    }

    override suspend fun deleteTrackFromFavourite(track: Track){
        appDatabase.trackDao().deleteTrack(trackDbConvertor.map(track))
    }
}