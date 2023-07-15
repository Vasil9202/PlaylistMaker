package com.example.playlistmaker.data.search.impl

import android.content.Context
import android.content.SharedPreferences
import androidx.activity.ComponentActivity
import com.example.playlistmaker.data.search.SharedPreferencesRepository
import com.example.playlistmaker.data.search.TrackPreferences
import com.example.playlistmaker.domain.search.model.Track

const val SEARCH_HISTORY = "search_history"


class SharedPreferencesRepositoryImpl(context: Context) :
    SharedPreferencesRepository {

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