package com.example.playlistmaker.domain.shared_pref.impl

import android.content.Context
import com.example.playlistmaker.domain.shared_pref.SharedPreferencesInteractor
import com.example.playlistmaker.data.shared_pref.impl.TrackPreferences
import com.example.playlistmaker.domain.model.Track

const val SEARCH_HISTORY = "search_history"


class SharedPreferencesInteractorImpl(context: Context) :
    SharedPreferencesInteractor {

    private val sharedPreferences = context.getSharedPreferences(SEARCH_HISTORY,Context.MODE_PRIVATE)

    override fun getSearchHistoryStorage() : List<Track>{
        return TrackPreferences.getInstance().read(sharedPreferences).toList()
    }

    override fun setSearchHistoryStorage(historyList: List<Track>) {
        TrackPreferences.getInstance().write(sharedPreferences, historyList.toTypedArray())
    }

    override fun clearSearchHistoryStorage(){
        TrackPreferences.getInstance().clear(sharedPreferences)
    }

}