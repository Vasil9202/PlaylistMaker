package com.example.playlistmaker.di

import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.impl.TracksInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    single<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }
}