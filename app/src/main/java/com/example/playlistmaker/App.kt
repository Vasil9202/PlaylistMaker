package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat

const val THEME_MODE = "theme"
const val DARK_THEME = false

class App : Application() {

    var darkTheme = false
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(THEME_MODE, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(THEME_MODE, DARK_THEME)
        if(darkTheme){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        sharedPrefs.edit().putBoolean(THEME_MODE, darkTheme).apply()
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}
