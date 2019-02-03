package com.mataku.scrobscrob.app

import android.app.Application
import org.koin.dsl.module.module

class App : Application() {

    private val modules = module {
        single { }
    }

    override fun onCreate() {
        super.onCreate()
    }
}