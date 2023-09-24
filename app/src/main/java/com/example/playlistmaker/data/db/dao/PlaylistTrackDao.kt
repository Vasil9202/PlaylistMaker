package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.FavouriteTrackEntity
import com.example.playlistmaker.data.db.entity.PlaylistTrackEntity

@Dao
interface PlaylistTrackDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: PlaylistTrackEntity)
    @Delete
    suspend fun deleteTrack(track: PlaylistTrackEntity)
    @Query("SELECT * FROM playlist_track_table ORDER BY timeInsert DESC")
    suspend fun getTracks(): List<PlaylistTrackEntity>
    @Query("SELECT trackId FROM playlist_track_table")
    suspend fun getTracksId(): List<String>
}