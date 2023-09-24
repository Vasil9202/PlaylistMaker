package com.example.playlistmaker.di

import com.example.playlistmaker.data.converters.PlaylistDbMapper
import com.example.playlistmaker.data.converters.PlaylistTrackDbMapper
import com.example.playlistmaker.data.converters.TrackDbMapper
import com.example.playlistmaker.data.favourite_tracks.impl.FavouriteTrackRepositoryImpl
import com.example.playlistmaker.data.player.impl.PlayerRepositoryImpl
import com.example.playlistmaker.data.playlist.impl.PlaylistRepositoryImpl
import com.example.playlistmaker.data.search.impl.TracksRepositoryImpl
import com.example.playlistmaker.data.settings.storage.impl.ThemeRepositoryImpl
import com.example.playlistmaker.domain.db.FavouriteTrackRepository
import com.example.playlistmaker.domain.db.PlaylistRepository
import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.settings.ThemeRepository
import org.koin.dsl.module

val repositoryModule = module {


    single<TracksRepository> {
        TracksRepositoryImpl(get(), get(), get(), get())
    }

    single<ThemeRepository> {
        ThemeRepositoryImpl(get())
    }

    single<PlayerRepository> {
        PlayerRepositoryImpl(get(), get())
    }

    factory { TrackDbMapper() }

    factory { PlaylistDbMapper() }

    factory { PlaylistTrackDbMapper() }

    single<FavouriteTrackRepository> {
        FavouriteTrackRepositoryImpl(get(), get())
    }
    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get(), get())
    }

}