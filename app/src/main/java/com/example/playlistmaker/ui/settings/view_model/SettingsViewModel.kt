package com.example.playlistmaker.ui.settings.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.util.App

class SettingsViewModel(private val app: Application) : ViewModel() {


    private val darkModeEnable = MutableLiveData(isDarkThemeEnabled())

    fun isDarkModeEnable(): LiveData<Boolean> = darkModeEnable

    fun themeChange(checked: Boolean){
        (app as App).switchTheme(checked)
        darkModeEnable.postValue(checked)
    }
    fun isDarkThemeEnabled() : Boolean = (app as App).isDarkThemeEnable()
}