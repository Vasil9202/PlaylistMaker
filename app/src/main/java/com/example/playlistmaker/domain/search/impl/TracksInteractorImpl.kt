package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.Executors

class TracksInteractorImpl (private val repository: TracksRepository) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>> {
        return repository.searchTracks(expression).map { result ->
            when(result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }


override fun writeStorage(track: List<Track>) {
        repository.writeStorage(track)
    }

    override fun readStorage(): List<Track> {
        return repository.readStorage()
    }

    override fun clear() {
        repository.clear()
    }
}