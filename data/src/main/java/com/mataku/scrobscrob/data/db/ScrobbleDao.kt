package com.mataku.scrobscrob.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScrobbleDao {

    @Query("SELECT count(id) FROM scrobble")
    fun getCount(): Long

    @Query("SELECT * FROM scrobble ORDER BY id DESC LIMIT :limit OFFSET :offset")
    fun getScrobbles(limit: Int, offset: Int): List<Scrobble>

    @Insert
    fun insert(scrobble: Scrobble)

}