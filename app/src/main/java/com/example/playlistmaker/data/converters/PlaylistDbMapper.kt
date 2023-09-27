package com.example.playlistmaker.data.converters

import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.domain.model.Playlist
import com.google.gson.Gson

class PlaylistDbMapper {
    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(playlist.id,
            playlist.name,
            playlist.description,
            playlist.placeholderPath,
            Gson().fromJson(playlist.tracksIdList, Array<String>::class.java).toMutableList(),
            playlist.tracksCount)
    }
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(playlist.id,
            playlist.name,
            playlist.description,
            playlist.placeholderPath,
            Gson().toJson(playlist.tracksIdList),
            playlist.tracksCount)
    }
}
