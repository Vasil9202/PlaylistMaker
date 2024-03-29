package com.example.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.ui.search.TracksState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class TracksSearchViewModel(
    private val tracksInteractor: TracksInteractor
) : ViewModel() {


    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
    private var latestSearchText: String? = null

    private var searchJob: Job? = null

    private val stateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = stateLiveData

    val historyVisibility: MutableLiveData<Boolean> = MutableLiveData(false)

    fun searchDebounce(changedText: String, updateBt: Boolean) {
        if (latestSearchText == changedText && !updateBt) {
            return
        }
        latestSearchText = changedText

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequest(changedText)
        }
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {

            renderState(TracksState.Loading)

            viewModelScope.launch {
                tracksInteractor
                    .searchTracks(newSearchText)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }

    private fun processResult(foundTracks: List<Track>?, errorMessage: String?) {
        val tracks = mutableListOf<Track>()
        if (foundTracks != null) {
            tracks.addAll(foundTracks)
        }
        when {
            errorMessage == R.string.net_error.toString() -> {
                renderState(TracksState.Error(errorMessage = R.string.net_error.toString()))
            }
            errorMessage == R.string.find_nothing.toString() -> {
                renderState(TracksState.Empty(message = R.string.find_nothing.toString()))
            }
            else -> {
                renderState(TracksState.Content(tracks = tracks))
            }
        }
    }

    private fun renderState(state: TracksState) {
        stateLiveData.postValue(state)
    }

    fun historyClearClick() {
        tracksInteractor.clear()
        historyVisibility.postValue(false)
    }

    fun addTrackToHistory(track: Track) {
        val historyList = ArrayList<Track>()
        historyList.addAll(tracksInteractor.readStorage())
        historyList.removeIf{it.trackId == track.trackId}
        historyList.add(0, track)
        if (historyList.size > 10) {
            historyList.removeAt(10)
        }
        tracksInteractor.writeStorage(historyList)
    }

    fun getSearchHistoryStorageList(): List<Track> {
        val list = tracksInteractor.readStorage()
        isTracksFavourite(list)
        return list
    }

    fun isTracksFavourite(tracks: List<Track>){
        viewModelScope.launch {
            tracksInteractor.isTracksFavourite(tracks)
        }
    }
}