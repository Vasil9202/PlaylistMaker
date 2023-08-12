package com.example.playlistmaker.di

import android.content.Context
import com.example.playlistmaker.data.player.impl.PlayerRepositoryImpl
import com.example.playlistmaker.data.search.impl.TracksRepositoryImpl
import com.example.playlistmaker.data.search.storage.TrackStorage
import com.example.playlistmaker.data.search.storage.sharedprefs.TrackStorageImpl
import com.example.playlistmaker.data.settings.storage.impl.ThemeRepositoryImpl
import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.settings.ThemeRepository
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {




    single<TracksRepository> {
        TracksRepositoryImpl(get(),get())
    }



    single<ThemeRepository> {
        ThemeRepositoryImpl(get())
    }


    single<PlayerRepository> {
        PlayerRepositoryImpl()
    }

}