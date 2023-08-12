package com.example.playlistmaker.domain.settings

interface ThemeRepository {

    fun saveTheme(theme: Boolean)

    fun isThemeDark(): Boolean

}