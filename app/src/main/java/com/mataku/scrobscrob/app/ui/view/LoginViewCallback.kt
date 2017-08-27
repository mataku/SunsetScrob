package com.mataku.scrobscrob.app.ui.view

interface LoginViewCallback {
    fun setSessionInfo(sessionKey: String, userName: String)
    fun showSuccessMessage()
    fun showMessageToAllowAccessToNotification()
    fun focusOnPasswordView()
    fun backToSettingsActivity()
}