package com.example.playlistmaker.data.player.impl

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.domain.player.PlayerRepository


class PlayerRepositoryImpl : PlayerRepository {

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


}