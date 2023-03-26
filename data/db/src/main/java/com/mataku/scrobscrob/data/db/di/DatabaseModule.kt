package com.mataku.scrobscrob.data.db.di

import androidx.room.Room
import com.mataku.scrobscrob.data.db.AppDatabase
import com.mataku.scrobscrob.data.db.ScrobbleAppDataStore
import com.mataku.scrobscrob.data.db.SessionKeyDataStore
import com.mataku.scrobscrob.data.db.ThemeDataStore
import com.mataku.scrobscrob.data.db.UsernameDataStore
import org.koin.dsl.module

val databaseModule = module {
  single {
    SessionKeyDataStore(get())
  }

  single {
    UsernameDataStore(get())
  }

  single {
    ThemeDataStore(get())
  }

  single {
    ScrobbleAppDataStore(get())
  }

  single {
    Room.databaseBuilder(get(), AppDatabase::class.java, "sunset_db").build()
  }

  single {
    get<AppDatabase>().nowPlayingDao
  }
}
