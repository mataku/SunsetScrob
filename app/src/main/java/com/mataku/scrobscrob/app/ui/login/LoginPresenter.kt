package com.mataku.scrobscrob.app.ui.login

import com.mataku.scrobscrob.app.data.repository.MobileSessionRepository
import com.mataku.scrobscrob.core.entity.presentation.onFailure
import com.mataku.scrobscrob.core.entity.presentation.onSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class LoginPresenter(
    private var accessible: Boolean,
    var view: LoginViewCallback,
    val repository: MobileSessionRepository
) {
    private val job = Job()
    private val coroutineContext = job + Dispatchers.Main

    fun auth(userName: String, password: String) {
        CoroutineScope(coroutineContext).launch {
            authenticate(userName, password)
        }
    }

    fun dispose() {
        coroutineContext.cancel()
    }

    private suspend fun authenticate(userName: String, password: String) {
        repository.authorize(userName, password)
            .onSuccess {
                view.setSessionInfo(it.key, it.name)
            }
            .onFailure {
                view.showError()
            }
    }

    fun backToSettingsWhenLoggedIn(success: Boolean, sessionKey: String) {
        if (success && sessionKey.isNotEmpty()) {
            if (accessible) {
                view.showSuccessMessage()
            } else {
                view.showSuccessMessage()
                view.showMessageToAllowAccessToNotification()
            }
            view.backToSettingsActivity()
        } else {
            view.focusOnPasswordView()
        }
    }
}