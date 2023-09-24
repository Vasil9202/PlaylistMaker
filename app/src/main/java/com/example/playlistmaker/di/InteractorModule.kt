package com.example.playlistmaker.di

import com.example.playlistmaker.domain.db.impl.FavouriteTrackInteractorImpl
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.settings.ThemeInteractor
import com.example.playlistmaker.domain.settings.impl.ThemeInteractorImpl
import com.example.playlistmaker.ui.db.FavouriteTrackInteractor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val interactorModule = module {

    single<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    single<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }

    single<ThemeInteractor> {
        ThemeInteractorImpl(get())
    }

    single<FavouriteTrackInteractor> {
        FavouriteTrackInteractorImpl(get())
    }
}