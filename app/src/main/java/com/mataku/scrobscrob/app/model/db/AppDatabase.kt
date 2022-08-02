package com.mataku.scrobscrob.app.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mataku.scrobscrob.app.data.db.ScrobbleDao

@Database(entities = [Scrobble::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
  abstract val scrobbleDao: ScrobbleDao
}
