package com.mataku.scrobscrob.app.ui.settings

interface SettingsViewCallback {
    fun setMessageToLogIn()
    fun setMessageToLogOut(loggedInUserName: String)
    fun setDestinationToMenuToLogIn()
    fun setDestinationToMenuToLogOut()
}