package com.example.playlistmaker.util

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

const val THEME_MODE = "theme"
const val DARK_THEME = false

class App : Application() {

    private var darkTheme : Boolean = false
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(THEME_MODE, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(THEME_MODE, DARK_THEME)
        if(darkTheme){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    fun isDarkThemeEnable():Boolean = darkTheme
    fun switchTheme(checked: Boolean){
        sharedPrefs.edit().putBoolean(THEME_MODE, checked).apply()
        AppCompatDelegate.setDefaultNightMode(
            if (checked) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}
