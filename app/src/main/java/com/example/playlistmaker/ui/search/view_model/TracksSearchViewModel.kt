package com.example.playlistmaker.ui.search.view_model

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.R
import com.example.playlistmaker.data.shared_pref.impl.SharedPreferencesRepositoryImpl
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.search.TracksState
import com.example.playlistmaker.util.Creator


class TracksSearchViewModel(
    application: Application
) : AndroidViewModel(application) {


    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()


        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                TracksSearchViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }

    private val tracksInteractor: TracksInteractor = Creator.provideTracksInteractor(application)

    private val sharedPreference = SharedPreferencesRepositoryImpl(application)

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
                                    errorMessage = getApplication<Application>().getString(R.string.net_error)
                                )
                            )
                        }

                        movies.isEmpty() -> {
                            renderState(
                                TracksState.Empty(
                                    message = getApplication<Application>().getString(R.string.find_nothing),
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
        sharedPreference.clear()
        historyVisibility.postValue(false)
    }

    fun addTrackToHistory(track: Track){
        val historyList = ArrayList<Track>()
        historyList.clear()
        historyList.addAll(sharedPreference.read())
        historyList.remove(track)
        historyList.add(0, track)
        if (historyList.size > 10) {
            historyList.removeAt(10)
        }
        sharedPreference.write(historyList)
    }

    fun getSearchHistoryStorageList(): List<Track> {
        return sharedPreference.read()
    }
}