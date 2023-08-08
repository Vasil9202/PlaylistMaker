package com.example.playlistmaker.di

import android.content.Context
import com.example.playlistmaker.data.player.impl.PlayerRepositoryImpl
import com.example.playlistmaker.data.search.impl.TracksRepositoryImpl
import com.example.playlistmaker.data.search.storage.TrackStorage
import com.example.playlistmaker.data.search.storage.sharedprefs.TrackStorageImpl
import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.search.TracksRepository
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    single {
        androidContext()
            .getSharedPreferences("local_storage", Context.MODE_PRIVATE)
    }

    factory { Gson() }


    single<TracksRepository> {
        TracksRepositoryImpl(get(),get())
    }

    single<TrackStorage> {
        TrackStorageImpl(get(),get())
    }


    single<PlayerRepository> {
        PlayerRepositoryImpl()
    }

}