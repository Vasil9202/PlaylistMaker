package com.example.playlistmaker.data.search


import com.example.playlistmaker.domain.search.model.Track

interface SharedPreferencesRepository {

    fun getSearchHistoryStorage(): List<Track>

    fun setSearchHistoryStorage(historyList: List<Track>)

    fun clearSearchHistoryStorage()
}
