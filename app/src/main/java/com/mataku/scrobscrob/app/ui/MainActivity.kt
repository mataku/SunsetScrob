package com.mataku.scrobscrob.app.ui

import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.app.service.AppleMusicNotificationReceiver

class MainActivity : AppCompatActivity() {
    private var receiver = AppleMusicNotificationReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val filter: IntentFilter = IntentFilter()
        filter.addAction("AppleMusic")
        registerReceiver(receiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}
