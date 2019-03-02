package com.mataku.scrobscrob.app.ui.top

class MainPresenter(var view: MainViewCallback) {
    fun showPreparationMenuIfNeeded(isEnabledReadNotification: Boolean) {
        if (!isEnabledReadNotification) {
            view.showNotificationAccessSettingMenu()
        }
    }
}