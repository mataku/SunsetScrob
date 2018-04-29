package com.mataku.scrobscrob.app.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.provider.Settings
import android.widget.Toast
import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.app.presenter.SettingsPresenter
import com.mataku.scrobscrob.app.ui.view.SettingsViewCallback

class SettingsActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentManager.beginTransaction().replace(
                android.R.id.content,
                SettingsFragment()
        ).commit()
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        setResult(RESULT_OK, intent)
        finish()
    }

    class SettingsFragment : PreferenceFragment(), SettingsViewCallback {
        private lateinit var loginPreference: Preference
        private lateinit var notificationPreference: Preference

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.preference)

            var sharedPreferences = this.activity.getSharedPreferences("DATA", Context.MODE_PRIVATE)
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
                    activity.finish()
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
            val intent = Intent(activity.applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }

        private fun showAlert() {
            val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this.activity)
            alertDialog.setTitle("ログアウト")
            alertDialog.setMessage("ログアウトしますか？")
            alertDialog.setPositiveButton(
                    "OK",
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, which: Int) {
                            removeSession()
                            fragmentManager.beginTransaction().replace(
                                    android.R.id.content,
                                    SettingsFragment()
                            ).commit()
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
            val sharedPreferences = activity.getSharedPreferences("DATA", Context.MODE_PRIVATE)
            sharedPreferences.edit().clear().apply()
        }

        private fun showLogOutMessage() {
            Toast.makeText(this.activity, "ログアウトしました", Toast.LENGTH_SHORT).show()
        }

        private fun showAllowServiceMessage() {
            Toast.makeText(this.activity, R.string.allow_notification_service_message, Toast.LENGTH_LONG).show()
        }
    }
}