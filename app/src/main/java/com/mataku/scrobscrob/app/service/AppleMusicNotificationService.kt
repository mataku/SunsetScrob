package com.mataku.scrobscrob.app.service

import android.content.pm.PackageManager
import android.service.notification.NotificationListenerService
import android.util.Log

class AppleMusicNotificationService: NotificationListenerService() {
    private val APPLE_PACKAGE_NAME = "com.apple.android.music"

    override fun onCreate() {
        super.onCreate()
        try {
            val appleMusicPackageInfo = packageManager.getPackageInfo(APPLE_PACKAGE_NAME, 0)
            Log.i("AppleMusicNotification", "Apple music is installed! (version: ${appleMusicPackageInfo.versionCode}")
        } catch (e: PackageManager.NameNotFoundException) {
            Log.i("AppleMusicNotification", "Apple music is NOT installed!")
        }
    }
}