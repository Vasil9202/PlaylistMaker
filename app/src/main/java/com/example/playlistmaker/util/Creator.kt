package com.example.playlistmaker.util

import android.content.Context
import com.example.playlistmaker.data.player.impl.PlayerRepositoryImpl
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.search.TracksRepository
import com.example.playlistmaker.domain.shared_pref.impl.SharedPreferencesInteractorImpl
import com.example.playlistmaker.data.search.impl.TracksRepositoryImpl
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.data.player.PlayerRepository
import com.example.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.search.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.model.Track



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

    fun provideTracksInteractor(context: Context): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository(context))
    }

    fun getSearchHistoryStorage(context: Context) : List<Track>{
        return SharedPreferencesInteractorImpl(context).getSearchHistoryStorage()
    }

    fun setSearchHistoryStorage(context: Context, historyList: List<Track>) {
        SharedPreferencesInteractorImpl(context).setSearchHistoryStorage(historyList)
    }

    fun clearSearchHistoryStorage(context: Context){
        SharedPreferencesInteractorImpl(context).clearSearchHistoryStorage()
    }

}