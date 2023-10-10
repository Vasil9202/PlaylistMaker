package com.example.playlistmaker.data.converters

import com.example.playlistmaker.data.db.entity.FavouriteTrackEntity
import com.example.playlistmaker.domain.model.Track

class FavouriteTrackDbMapper {

    fun map(track: FavouriteTrackEntity): Track {
        return Track(track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMin,
            track.trackTimeMilliSec,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl)
    }

    fun map(track: Track): FavouriteTrackEntity {
        return FavouriteTrackEntity(track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMin,
            track.trackTimeMilliSec,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl)
    }

}
