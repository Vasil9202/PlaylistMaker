package com.example.playlistmaker.ui.settings.view_model

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.App
import com.example.playlistmaker.ui.search.view_model.TracksSearchViewModel
import com.example.playlistmaker.util.Creator

class SettingsViewModel(application: Application
) : AndroidViewModel(application) {


    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
            }
        }
    }

    val darkModeEnable: MutableLiveData<Boolean> = MutableLiveData(isDarkThemeEnabled())

    fun themeChange(checked: Boolean){
        (getApplication() as App).switchTheme(checked)
        darkModeEnable.postValue(checked)
    }
    fun isDarkThemeEnabled() : Boolean = ((getApplication() as App).isDarkThemeEnable())
}