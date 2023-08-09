package com.example.playlistmaker.domain.settings

interface ThemeInteractor {

    fun saveTheme(theme: Boolean)

    fun isThemeDark(): Boolean

}