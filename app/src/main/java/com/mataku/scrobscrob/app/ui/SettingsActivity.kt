package com.mataku.scrobscrob.app.ui

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceActivity
import android.provider.Settings
import android.widget.Toast
import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.app.presenter.SettingsPresenter
import com.mataku.scrobscrob.app.ui.view.SettingsViewCallback

class SettingsActivity : PreferenceActivity(), SettingsViewCallback {

    private lateinit var loginPreference: Preference
    private lateinit var notificationPreference: Preference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference)

        var sharedPreferences = getSharedPreferences("DATA", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("UserName", "")

        val presenter = SettingsPresenter(this)
        presenter.setMessageAccordingToUserStatus(userName)
        presenter.setDestinationAccordingToUserStatus(userName)
    }

    override fun setMessageToLogIn() {
        loginPreference = findPreference("login")
        loginPreference.title = "Last.fm へログイン"
        loginPreference.summary = ""
    }

    override fun setMessageToLogOut(loggedInUserName: String) {
        loginPreference = findPreference("login")
        loginPreference.title = "ログアウト"
        loginPreference.summary = "${loggedInUserName} でログインしています"
    }

    override fun setDestinationToMenuToLogIn() {
        loginPreference = findPreference("login")
        loginPreference.onPreferenceClickListener = object : Preference.OnPreferenceClickListener {
            override fun onPreferenceClick(preference: Preference): Boolean {
                showSettingsActivity()
                return true
            }
        }
    }

    override fun setDestinationToMenuToLogOut() {
        loginPreference = findPreference("login")
        loginPreference.onPreferenceClickListener = object : Preference.OnPreferenceClickListener {
            override fun onPreferenceClick(preference: Preference): Boolean {
                showAlert()
                return true
            }
        }
    }

    private fun showNotificationAccessSettingMenu() {
        val intent = Intent()
        intent.action = Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
        startActivity(intent)
    }

    private fun showSettingsActivity() {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun showAlert() {
        val alertDIalog: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDIalog.setTitle("ログアウト")
        alertDIalog.setMessage("ログアウトしますか？")
        alertDIalog.setPositiveButton(
                "OK",
                object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        removeSession()
                        recreate()
                        showLogOutMessage()
                    }
                }
        )

        alertDIalog.setNegativeButton(
                "Cancel",
                object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {

                    }
                }
        )

        alertDIalog.create().show()
    }

    private fun removeSession() {
        val sharedPreferences = getSharedPreferences("DATA", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }

    private fun showLogOutMessage() {
        Toast.makeText(this, "ログアウトしました", Toast.LENGTH_SHORT).show()
    }
}