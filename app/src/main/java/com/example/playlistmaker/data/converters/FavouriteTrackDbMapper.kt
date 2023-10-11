package com.example.playlistmaker.data.converters

import com.example.playlistmaker.data.db.entity.FavouriteTrackEntity
import com.example.playlistmaker.domain.model.Track

class FavouriteTrackDbMapper {

    fun map(favoriteTrack: FavouriteTrackEntity): Track {
        return Track(favoriteTrack.trackId,
            favoriteTrack.trackName,
            favoriteTrack.artistName,
            favoriteTrack.trackTimeMilliSec,
            favoriteTrack.artworkUrl100,
            favoriteTrack.collectionName,
            favoriteTrack.releaseDate,
            favoriteTrack.primaryGenreName,
            favoriteTrack.country,
            favoriteTrack.previewUrl)
    }

    fun map(track: Track): FavouriteTrackEntity {
        return FavouriteTrackEntity(track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMilliSec,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl)
    }

}
