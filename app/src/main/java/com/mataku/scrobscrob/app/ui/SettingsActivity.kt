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
        notificationPreference = findPreference("notification")
        notificationPreference.onPreferenceClickListener = object : Preference.OnPreferenceClickListener {
            override fun onPreferenceClick(preference: Preference): Boolean {
                val intent = Intent()
                intent.action = Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
                startActivity(intent)
                showAllowServiceMessage()
                return true
            }
        }
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
                finish()
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
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialog.setTitle("ログアウト")
        alertDialog.setMessage("ログアウトしますか？")
        alertDialog.setPositiveButton(
                "OK",
                object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        removeSession()
                        recreate()
                        showLogOutMessage()
                    }
                }
        )

        alertDialog.setNegativeButton(
                "Cancel",
                object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {

                    }
                }
        )

        alertDialog.create().show()
    }

    private fun removeSession() {
        val sharedPreferences = getSharedPreferences("DATA", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }

    private fun showLogOutMessage() {
        Toast.makeText(this, "ログアウトしました", Toast.LENGTH_SHORT).show()
    }

    private fun showAllowServiceMessage() {
        Toast.makeText(this, R.string.allow_notification_service_message, Toast.LENGTH_SHORT).show()
    }
}