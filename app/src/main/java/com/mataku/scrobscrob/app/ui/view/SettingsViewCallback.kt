package com.mataku.scrobscrob.app.ui.view

interface SettingsViewCallback {
    fun setMessageToLogIn()
    fun setMessageToLogOut(loggedInUserName: String)
    fun setDestinationToMenuToLogIn()
    fun setDestinationToMenuToLogOut()
}