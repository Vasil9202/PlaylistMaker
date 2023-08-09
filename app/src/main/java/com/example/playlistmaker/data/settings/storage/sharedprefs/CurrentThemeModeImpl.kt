package com.example.playlistmaker.data.settings.storage.sharedprefs

import android.content.Context
import com.example.playlistmaker.data.settings.storage.CurrentThemeMode


const val CURRENT_THEME = "current"
const val THEME = "theme"
class CurrentThemeModeImpl(private val context: Context) : CurrentThemeMode{

    private val sharedPreferences = context.getSharedPreferences(THEME, Context.MODE_PRIVATE)

    override fun isThemeDark(): Boolean {
        return sharedPreferences.getBoolean(CURRENT_THEME, false)
    }

    override fun saveTheme(theme: Boolean) {
        sharedPreferences.edit().putBoolean(CURRENT_THEME,theme).apply()
    }
}