package com.example.playlistmaker.domain.shared_pref


import com.example.playlistmaker.domain.model.Track

interface SharedPreferencesInteractor {

    fun getSearchHistoryStorage(): List<Track>

    fun setSearchHistoryStorage(historyList: List<Track>)

    fun clearSearchHistoryStorage()
}
