package com.example.playlistmaker.ui.media_lib.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.search.TracksInteractor
import kotlinx.coroutines.launch

class FeaturedTracksViewModel(
    private val tracksInteractor: TracksInteractor
) : ViewModel() {


    private val favouriteTracks = MutableLiveData<List<Track>>(emptyList())

    private val favouriteTracksEmpty = MutableLiveData<Boolean>(false)

    fun getFavouriteTracks(): LiveData<List<Track>> = favouriteTracks
    fun isFavouriteTracksEmpty(): LiveData<Boolean> = favouriteTracksEmpty

    fun statusViewOff(){
        favouriteTracksEmpty.postValue(false)
        favouriteTracks.postValue(emptyList())
    }
    fun updateFavouriteTracks() {
        viewModelScope.launch {
            val list = tracksInteractor.getFavouriteTracks()
            if(list.isEmpty()){
                favouriteTracksEmpty.postValue(true)
            } else{
                list.map { it -> it.isFavorite = true }
                favouriteTracks.postValue(list)
            }
        }
    }

}