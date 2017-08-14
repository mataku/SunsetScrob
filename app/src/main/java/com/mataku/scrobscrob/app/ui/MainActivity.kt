package com.mataku.scrobscrob.app.ui

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.app.service.AppleMusicNotificationReceiver

class MainActivity : AppCompatActivity() {
    private var receiver = AppleMusicNotificationReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isEnabledReadNotification()) {
            showNotificationAccessSettingMenu()
        }
        setContentView(R.layout.activity_main)
        val filter: IntentFilter = IntentFilter()
        filter.addAction("AppleMusic")
        registerReceiver(receiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private fun showNotificationAccessSettingMenu() {
        val intent = Intent()
        intent.action = Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
        startActivity(intent)
    }

    private fun isEnabledReadNotification(): Boolean {
        val contentResolver = contentResolver
        val rawListeners = Settings.Secure.getString(contentResolver,
                "enabled_notification_listeners")
        if (rawListeners == null || "" == rawListeners) {
            return false
        } else {
            val listeners = rawListeners.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (listener in listeners) {
                if (listener.startsWith(packageName)) {
                    return true
                }
            }
        }
        return false
    }
}
