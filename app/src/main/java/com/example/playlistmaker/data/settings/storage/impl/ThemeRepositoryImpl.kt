package com.example.playlistmaker.data.settings.storage.impl

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.data.settings.storage.CurrentThemeMode
import com.example.playlistmaker.domain.settings.ThemeRepository
import com.example.playlistmaker.util.App


const val CURRENT_THEME = "current"
const val THEME = "theme"
class ThemeRepositoryImpl(private val currentTheme: CurrentThemeMode) :
    ThemeRepository {


    override fun isThemeDark(): Boolean {
        return currentTheme.isThemeDark()
    }

    override fun saveTheme(theme: Boolean) {
        currentTheme.saveTheme(theme)
    }


}