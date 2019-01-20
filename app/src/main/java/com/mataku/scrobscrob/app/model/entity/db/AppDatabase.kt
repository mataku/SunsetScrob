package com.mataku.scrobscrob.app.model.entity.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mataku.scrobscrob.app.data.db.ScrobbleDao
import com.mataku.scrobscrob.app.model.entity.Scrobble

@Database(entities = [Scrobble::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val scrobbleDao: ScrobbleDao
}