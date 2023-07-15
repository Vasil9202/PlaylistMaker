package com.example.playlistmaker.util

import android.content.Context
import android.content.SharedPreferences
import androidx.activity.ComponentActivity
import com.example.playlistmaker.data.PlayerRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.search.TRACK_KEY
import com.example.playlistmaker.data.search.TrackPreferences
import com.example.playlistmaker.data.search.TracksRepository
import com.example.playlistmaker.data.search.impl.SharedPreferencesRepositoryImpl
import com.example.playlistmaker.data.search.impl.TracksRepositoryImpl
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.PlayerRepository
import com.example.playlistmaker.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.model.Track



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
        return SharedPreferencesRepositoryImpl(context).getSearchHistoryStorage()
    }

    fun setSearchHistoryStorage(context: Context, historyList: List<Track>) {
        SharedPreferencesRepositoryImpl(context).setSearchHistoryStorage(historyList)
    }

    fun clearSearchHistoryStorage(context: Context){
        SharedPreferencesRepositoryImpl(context).clearSearchHistoryStorage()
    }

}