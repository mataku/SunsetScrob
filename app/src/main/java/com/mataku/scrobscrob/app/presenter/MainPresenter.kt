package com.mataku.scrobscrob.app.presenter

import com.mataku.scrobscrob.app.ui.view.MainViewCallback

class MainPresenter(var view: MainViewCallback) {
    fun showPreparationMenuIfNeeded(isEnabledReadNotification: Boolean) {
        if (!isEnabledReadNotification) {
            view.showNotificationAccessSettingMenu()
        }
    }
}