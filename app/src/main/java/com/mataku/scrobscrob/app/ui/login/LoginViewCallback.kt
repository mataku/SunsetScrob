package com.mataku.scrobscrob.app.ui.login

interface LoginViewCallback {
    fun setSessionInfo(sessionKey: String, userName: String)
    fun showSuccessMessage()
    fun showMessageToAllowAccessToNotification()
    fun focusOnPasswordView()
    fun backToSettingsActivity()
    fun showError()
}