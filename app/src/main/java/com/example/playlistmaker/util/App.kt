package com.example.playlistmaker.util

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

const val CURRENT_THEME = "current"
const val THEME = "theme"
class App() : Application() {


private lateinit var sharedPreferences:SharedPreferences
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }
        sharedPreferences = this.getSharedPreferences(com.example.playlistmaker.data.settings.storage.sharedprefs.THEME, Context.MODE_PRIVATE)
        if(sharedPreferences.getBoolean(CURRENT_THEME, false))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

    }
}
