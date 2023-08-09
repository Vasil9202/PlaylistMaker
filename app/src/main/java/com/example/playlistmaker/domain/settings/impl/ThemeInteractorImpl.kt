package com.example.playlistmaker.domain.settings.impl

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.settings.ThemeInteractor
import com.example.playlistmaker.domain.settings.ThemeRepository
import com.example.playlistmaker.util.App

class ThemeInteractorImpl(private val repository: ThemeRepository) : ThemeInteractor {

    override fun saveTheme(theme: Boolean) {
        repository.saveTheme(theme)
        if(theme){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun isThemeDark(): Boolean {
        return repository.isThemeDark()
    }
}