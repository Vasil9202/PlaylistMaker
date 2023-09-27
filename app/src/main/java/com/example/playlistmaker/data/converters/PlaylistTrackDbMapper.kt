package com.example.playlistmaker.data.converters

import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.db.entity.PlaylistTrackEntity
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.google.gson.Gson

class PlaylistTrackDbMapper {
    fun map(trackEntity: PlaylistTrackEntity): Track {
        return Track(trackEntity.trackId,
            trackEntity.trackName,
            trackEntity.artistName,
            trackEntity.trackTimeMin,
            trackEntity.artworkUrl100,
            trackEntity.collectionName,
            trackEntity.releaseDate,
            trackEntity.primaryGenreName,
            trackEntity.country,
            trackEntity.previewUrl)
    }
    fun map(track: Track): PlaylistTrackEntity {
        return PlaylistTrackEntity(track.trackId,
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
