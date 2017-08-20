package com.mataku.scrobscrob.app.presenter

import com.mataku.scrobscrob.app.model.api.Retrofit2LastFmClient
import com.mataku.scrobscrob.app.ui.view.LoginViewCallback
import com.mataku.scrobscrob.app.util.Settings
import java.math.BigInteger
import java.security.MessageDigest

class LoginPresenter(var view: LoginViewCallback) {
    private val appSettings = Settings()
    private val method = "auth.getMobileSession"

    fun authenticate(userName: String, password: String) {
        val apiSig: String = generateApiSig(userName, password)
        val client = Retrofit2LastFmClient.createService()
        val call = client.authenticate(userName, password, appSettings.apiKey, apiSig)
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
            view.showSuccessMessage()
            view.backToSettingsActivity()
        } else {
            view.focusOnPasswordView()
        }
    }

    private fun generateApiSig(userName: String, password: String): String {
        val str = "api_key${appSettings.apiKey}method${method}password${password}username${userName}${appSettings.sharedSecret}"
        var md5Str = ""
        try {
            var strBytes: ByteArray = str.toByteArray(charset("UTF-8"))
            val md = MessageDigest.getInstance("MD5")
            val md5Bytes = md.digest(strBytes)
            val bigInt = BigInteger(1, md5Bytes)
            md5Str = bigInt.toString(16)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return md5Str
    }
}