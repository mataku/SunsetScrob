package com.mataku.scrobscrob.app.ui

import android.content.Intent
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceActivity
import android.provider.Settings
import com.mataku.scrobscrob.R

class SettingsActivity : PreferenceActivity() {

    lateinit var loginPreference: Preference
    lateinit var notificationPreference: Preference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference)

        loginPreference = findPreference("login")
        loginPreference.onPreferenceClickListener = object : Preference.OnPreferenceClickListener {
            override fun onPreferenceClick(preference: Preference): Boolean {
                showSettings()
                return true
            }
        }

        notificationPreference = findPreference("notification")
        notificationPreference.onPreferenceClickListener = object : Preference.OnPreferenceClickListener {
            override fun onPreferenceClick(preference: Preference): Boolean {
                showNotificationAccessSettingMenu()
                return true
            }
        }
    }

    private fun showNotificationAccessSettingMenu() {
        val intent = Intent()
        intent.action = Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
        startActivity(intent)
    }

    private fun showSettings() {
        val intent = Intent(applicationContext, SettingsActivity::class.java)
        startActivity(intent)
    }
}