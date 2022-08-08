package com.mataku.scrobscrob.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface NowPlayingDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertNowPlaying(entity: NowPlayingTrackEntity)

  @Transaction
  suspend fun insert(nowPlaying: NowPlayingTrackEntity) {
    deleteNowPlaying()
    insertNowPlaying(nowPlaying)
  }

  @Query("DELETE FROM NowPlayingTrackEntity")
  suspend fun deleteNowPlaying()

  @Query("SELECT * FROM NowPlayingTrackEntity limit 1")
  suspend fun nowPlaying(): NowPlayingTrackEntity?
}
