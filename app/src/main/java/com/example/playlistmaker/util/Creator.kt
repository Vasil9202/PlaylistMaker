package com.example.playlistmaker.util

import android.content.Context
import com.example.playlistmaker.data.player.impl.PlayerRepositoryImpl
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.data.search.impl.TracksRepositoryImpl
import com.example.playlistmaker.data.shared_pref.SharedPreferencesRepository
import com.example.playlistmaker.data.shared_pref.impl.SharedPreferencesRepositoryImpl
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.search.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.search.TracksInteractor


object Creator {

    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }


    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }

    private fun getTracksRepository(context: Context): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(context))
    }
    private fun getSharedPreferencesRepository(context: Context): SharedPreferencesRepository {
        return SharedPreferencesRepositoryImpl(context)
    }

    fun provideTracksInteractor(context: Context): TracksInteractor {
        return TracksInteractorImpl(getSharedPreferencesRepository(context),getTracksRepository(context))
    }

}