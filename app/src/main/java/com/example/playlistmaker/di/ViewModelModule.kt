package com.example.playlistmaker.di

import com.example.playlistmaker.ui.media_lib.view_model.FeaturedTracksViewModel
import com.example.playlistmaker.ui.media_lib.view_model.PlaylistViewModel
import com.example.playlistmaker.ui.player.view_model.PlayerActivityViewModel
import com.example.playlistmaker.ui.search.view_model.TracksSearchViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        TracksSearchViewModel(get())
    }

    viewModel() {
        PlayerActivityViewModel(get(),get())
    }

    viewModel() {
        SettingsViewModel(get())
    }

    viewModel() {
        FeaturedTracksViewModel(get())
    }

    viewModel() {
        PlaylistViewModel(get())
    }
}