package com.mataku.scrobscrob.app.presenter

import android.content.Context
import android.provider.Settings
import com.mataku.scrobscrob.app.model.api.Retrofit2LastFmClient
import com.mataku.scrobscrob.app.ui.view.LoginViewCallback
import com.mataku.scrobscrob.app.util.AppUtil

class LoginPresenter(var context: Context, var view: LoginViewCallback) {
    private val appUtil = AppUtil()
    private val method = "auth.getMobileSession"

    fun authenticate(userName: String, password: String) {
        var params: MutableMap<String, String> = mutableMapOf()
        params["username"] = userName
        params["password"] = password
        params["method"] = method
        params["api_key"] = appUtil.apiKey

        val apiSig: String = appUtil.generateApiSig(params)
        val client = Retrofit2LastFmClient.createService()
        val call = client.authenticate(userName, password, appUtil.apiKey, apiSig)
        try {
            val response = call.execute()
            val mobileSession = response?.body()?.mobileSession
            view.setSessionInfo(mobileSession!!.key, mobileSession!!.name)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun backToSettingsWhenLoggedIn(success: Boolean?, sessionKey: String) {
        if (success!! && sessionKey.isNotEmpty()) {
            if (isEnabledReadNotification()) {
                view.showSuccessMessage()
            } else {
                view.showSuccessMessage()
                view.showMessageToAllowAccessToNotification()
            }
            view.backToSettingsActivity()
        } else {
            view.focusOnPasswordView()
        }
    }

    private fun isEnabledReadNotification(): Boolean {
        val contentResolver = context.contentResolver
        val rawListeners = Settings.Secure.getString(contentResolver,
                "enabled_notification_listeners")
        if (rawListeners == null || rawListeners == "") {
            return false
        } else {
            val listeners = rawListeners.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            listeners
                    .filter { it.startsWith(context.packageName) }
                    .forEach { return true }
        }
        return false
    }
}