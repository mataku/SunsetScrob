package com.mataku.scrobscrob.app.ui

import android.content.Intent
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceActivity
import com.mataku.scrobscrob.R

class SettingsActivity : PreferenceActivity() {

    lateinit var loginPreference: Preference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference)

        loginPreference = findPreference("login")
        loginPreference.onPreferenceClickListener = object : Preference.OnPreferenceClickListener {
            override fun onPreferenceClick(preference: Preference): Boolean {
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
                return true
            }
        }
    }
}