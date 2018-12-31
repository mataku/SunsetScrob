package com.mataku.scrobscrob.app.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mataku.scrobscrob.app.model.entity.Scrobble

@Dao
interface ScrobbleDao {

    @Query("SELECT count(id) FROM scrobble")
    fun getCount(): Long

    @Query("SELECT * FROM scrobble ORDER BY id DESC")
    fun getScrobbles(): List<Scrobble>

    @Insert
    fun insert(scrobble: Scrobble)
}