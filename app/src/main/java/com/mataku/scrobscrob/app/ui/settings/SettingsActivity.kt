package com.mataku.scrobscrob.app.ui.settings

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.app.model.Scrobble
import com.mataku.scrobscrob.app.presenter.SettingsPresenter
import com.mataku.scrobscrob.app.ui.LicensesActivity
import com.mataku.scrobscrob.app.ui.MainActivity
import com.mataku.scrobscrob.app.ui.login.LoginActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction().replace(
            android.R.id.content,
            SettingsFragment()
        ).commit()
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        setResult(RESULT_OK, intent)
        finish()
    }

    class SettingsFragment : PreferenceFragmentCompat(), SettingsViewCallback {
        private lateinit var loginPreference: Preference
        private lateinit var licensesPreference: Preference

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.preference)
            val presenter = SettingsPresenter(this)

            val sharedPreferences = this.activity?.getSharedPreferences("DATA", Context.MODE_PRIVATE)
            val userName = sharedPreferences?.getString("UserName", "")
            userName?.let {
                presenter.setMessageAccordingToUserStatus(it)
                presenter.setDestinationAccordingToUserStatus(it)
            }

            val notificationPreference = findPreference("notification")
            notificationPreference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                val intent = Intent()
                intent.action = Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
                startActivity(intent)
                showAllowServiceMessage()
                true
            }

            licensesPreference = findPreference("licenses")
            licensesPreference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                val intent = Intent(this@SettingsFragment.context, LicensesActivity::class.java)
                startActivity(intent)
                true
            }
        }

        override fun setMessageToLogIn() {
            loginPreference = findPreference("login")
            loginPreference.title = "Login to Last.fm"
            loginPreference.summary = ""
        }

        override fun setMessageToLogOut(loggedInUserName: String) {
            loginPreference = findPreference("login")
            loginPreference.title = "Logout"
            loginPreference.summary = "Login as $loggedInUserName"
        }

        override fun setDestinationToMenuToLogIn() {
            loginPreference = findPreference("login")
            loginPreference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                activity?.finish()
                showSettingsActivity()
                true
            }
        }

        override fun setDestinationToMenuToLogOut() {
            loginPreference = findPreference("login")
            loginPreference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                showAlert()
                true
            }
        }

        private fun showSettingsActivity() {
            activity?.let {
                val intent = Intent(it.applicationContext, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        private fun showAlert() {
            val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this.activity)
            alertDialog.setTitle("Logout")
            alertDialog.setMessage("Really?")
            alertDialog.setPositiveButton(
                "OK"
            ) { _, _ ->
                removeSession()
                Scrobble().deleteAll()

                fragmentManager?.beginTransaction()?.replace(
                    android.R.id.content,
                    SettingsFragment()
                )?.commit()
                showLogOutMessage()
            }

            alertDialog.setNegativeButton(
                "Cancel"
            ) { _, _ -> }

            alertDialog.create().show()
        }

        private fun removeSession() {
            val sharedPreferences = activity?.getSharedPreferences("DATA", Context.MODE_PRIVATE)
            sharedPreferences?.edit()?.clear()?.apply()
        }

        private fun showLogOutMessage() {
            Toast.makeText(this.activity, "Logged out", Toast.LENGTH_SHORT).show()
        }

        private fun showAllowServiceMessage() {
            Toast.makeText(this.activity, R.string.allow_notification_service_message, Toast.LENGTH_LONG).show()
        }
    }
}