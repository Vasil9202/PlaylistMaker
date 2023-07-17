package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.data.shared_pref.SharedPreferencesRepository
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.util.Resource
import java.util.concurrent.Executors

class TracksInteractorImpl (private val sharedRepository: SharedPreferencesRepository, private val repository: TracksRepository) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            when(val resource = repository.searchTracks(expression)) {
                is Resource.Success -> { consumer.consume(resource.data, null) }
                is Resource.Error -> { consumer.consume(null, resource.message) }
            }
        }
    }

    override fun writeStorage(track: List<Track>) {
        sharedRepository.writeStorage(track)
    }

    override fun readStorage(): List<Track> {
        return sharedRepository.readStorage()
    }

    override fun clear() {
        sharedRepository.clear()
    }
}