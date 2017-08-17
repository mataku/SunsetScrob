package com.mataku.scrobscrob.app.ui.view

interface LoginViewCallback {
    fun setSessionKey(sessionKey: String)
    fun showSuccessMessage()
    fun focusOnPasswordView()
}