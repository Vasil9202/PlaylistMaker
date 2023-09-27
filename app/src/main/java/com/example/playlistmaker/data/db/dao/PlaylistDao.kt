package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.PlaylistEntity

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)
    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)
    @Query("SELECT * FROM playlist_table")
    suspend fun getAllPlaylist(): List<PlaylistEntity>
    //@Query("SELECT tracksIdList FROM playlist_table")
    //suspend fun getPlaylistTracksId(playlist: String): String
}

