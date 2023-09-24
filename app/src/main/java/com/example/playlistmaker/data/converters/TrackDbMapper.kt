package com.example.playlistmaker.data.converters

import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.domain.model.Track

class TrackDbMapper {

    fun map(track: TrackEntity): Track {
        return Track(track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMin,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl)
    }

    fun map(track: Track): TrackEntity {
        return TrackEntity(track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMin,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl)
    }

}
