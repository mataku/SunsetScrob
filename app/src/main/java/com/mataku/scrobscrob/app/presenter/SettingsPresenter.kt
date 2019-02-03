package com.mataku.scrobscrob.app.presenter

import com.mataku.scrobscrob.app.ui.settings.SettingsViewCallback

class SettingsPresenter(var view: SettingsViewCallback) {
    fun setMessageAccordingToUserStatus(loggedInUserName: String) {
        if (loggedInUserName.isEmpty()) {
            view.setMessageToLogIn()
        } else {
            view.setMessageToLogOut(loggedInUserName)
        }
    }

    fun setDestinationAccordingToUserStatus(loggedInUserName: String) {
        if (loggedInUserName.isEmpty()) {
            view.setDestinationToMenuToLogIn()
        } else {
            view.setDestinationToMenuToLogOut()
        }
    }
}