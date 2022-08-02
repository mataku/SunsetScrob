package com.mataku.scrobscrob.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Scrobble::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
  abstract val scrobbleDao: ScrobbleDao
}
