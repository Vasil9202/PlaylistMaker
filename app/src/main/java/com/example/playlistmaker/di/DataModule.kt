package com.example.playlistmaker.di

import android.content.Context
import com.example.playlistmaker.data.search.network.ITunesApiService
import com.example.playlistmaker.data.search.network.NetworkClient
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.search.storage.TrackStorage
import com.example.playlistmaker.data.search.storage.sharedprefs.TrackStorageImpl
import com.example.playlistmaker.data.settings.storage.CurrentThemeMode
import com.example.playlistmaker.data.settings.storage.sharedprefs.CurrentThemeModeImpl
import com.google.gson.Gson
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<ITunesApiService> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApiService::class.java)
    }

    single<TrackStorage> {
        TrackStorageImpl(androidContext(), Gson())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(androidContext())
    }

    single<CurrentThemeMode> {
        CurrentThemeModeImpl(androidContext())
    }

}