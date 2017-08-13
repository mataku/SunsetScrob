package com.mataku.scrobscrob.app.service

import android.content.Intent
import android.content.pm.PackageManager
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class AppleMusicNotificationService : NotificationListenerService() {
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

    override fun onDestroy() {
        super.onDestroy()
    }

    // ステータスバーに通知が更新される
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)
        Log.i("Posted", "Updated!")

        var intent: Intent = Intent("AppleMusic")
        intent.putExtra("message", "Yes!!!")
        sendBroadcast(intent)

        // TODO: 曲情報の取得処理を書く
    }

    // ステータスバーから通知が消去される
    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        super.onNotificationRemoved(sbn)
        Log.i("Posted", "Removed")

        var intent: Intent = Intent("AppleMusic")
        intent.putExtra("message", "Yes!!!")
        sendBroadcast(intent)
    }
}