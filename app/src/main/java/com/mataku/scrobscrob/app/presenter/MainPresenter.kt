package com.mataku.scrobscrob.app.presenter

import com.mataku.scrobscrob.app.ui.view.MainViewCallback

class MainPresenter(var view: MainViewCallback) {
    fun showPreparationMenuIfNeeded(userName: String, isEnabledReadNotification: Boolean) {
        if (userName.isEmpty()) {
            view.showLoginActivity()
        } else if (isEnabledReadNotification) {
            view.showNotificationAccessSettingMenu()
        }
    }
}