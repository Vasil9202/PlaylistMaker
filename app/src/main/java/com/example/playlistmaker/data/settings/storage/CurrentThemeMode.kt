package com.example.playlistmaker.data.settings.storage

import com.example.playlistmaker.data.search.storage.model.TracksList

    interface CurrentThemeMode {

        fun saveTheme(theme: Boolean)

        fun isThemeDark(): Boolean

    }