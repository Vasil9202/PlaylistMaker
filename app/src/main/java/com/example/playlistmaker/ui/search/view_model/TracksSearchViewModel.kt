package com.example.playlistmaker.ui.search.view_model

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.search.TracksState


class TracksSearchViewModel(private val tracksInteractor: TracksInteractor
) : ViewModel() {


    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }



    private val handler = Handler(Looper.getMainLooper())

    private val stateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = stateLiveData

    private var latestSearchText: String? = null




    val historyVisibility: MutableLiveData<Boolean> = MutableLiveData(false)


    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun searchDebounce(changedText: String?) {
        if (changedText == null || latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { searchRequest(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(TracksState.Loading)

            tracksInteractor.searchTracks(newSearchText, object : TracksInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                    val movies = mutableListOf<Track>()
                    if (foundTracks != null) {
                        movies.addAll(foundTracks)
                    }

                    when {
                        errorMessage != null -> {
                            renderState(
                                TracksState.Error(
                                    errorMessage = R.string.net_error.toString()
                                )
                            )
                        }

                        movies.isEmpty() -> {
                            renderState(
                                TracksState.Empty(
                                    message = R.string.find_nothing.toString(),
                                )
                            )
                        }

                        else -> {
                            renderState(
                                TracksState.Content(
                                    movies = movies,
                                )
                            )
                        }
                    }

                }
            })
        }
    }

    private fun renderState(state: TracksState) {
        stateLiveData.postValue(state)
    }

    fun historyClearClick(){
        tracksInteractor.clear()
        historyVisibility.postValue(false)
    }

    fun addTrackToHistory(track: Track){
        val historyList = ArrayList<Track>()
        historyList.clear()
        historyList.addAll(tracksInteractor.readStorage())
        historyList.remove(track)
        historyList.add(0, track)
        if (historyList.size > 10) {
            historyList.removeAt(10)
        }
        tracksInteractor.writeStorage(historyList)
    }

    fun getSearchHistoryStorageList(): List<Track> {
        return tracksInteractor.readStorage()
    }
}