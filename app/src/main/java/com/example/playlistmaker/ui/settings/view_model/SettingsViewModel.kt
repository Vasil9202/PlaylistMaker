package com.example.playlistmaker.ui.settings.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.util.App

class SettingsViewModel(application: Application
) : AndroidViewModel(application) {


    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
            }
        }
    }

    val darkModeEnable = MutableLiveData(isDarkThemeEnabled())

    fun isDarkModeEnable(): LiveData<Boolean> = darkModeEnable

    fun themeChange(checked: Boolean){
        (getApplication() as App).switchTheme(checked)
        darkModeEnable.postValue(checked)
    }
    fun isDarkThemeEnabled() : Boolean = ((getApplication() as App).isDarkThemeEnable())
}