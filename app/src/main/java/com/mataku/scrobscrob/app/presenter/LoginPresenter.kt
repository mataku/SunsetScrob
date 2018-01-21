package com.mataku.scrobscrob.app.presenter

import com.mataku.scrobscrob.app.model.api.Retrofit2LastFmClient
import com.mataku.scrobscrob.app.model.api.service.AuthMobileSessionService
import com.mataku.scrobscrob.app.model.entity.MobileSessionApiResponse
import com.mataku.scrobscrob.app.ui.view.LoginViewCallback
import com.mataku.scrobscrob.app.util.AppUtil
import kotlinx.coroutines.experimental.runBlocking
import ru.gildor.coroutines.retrofit.Result
import ru.gildor.coroutines.retrofit.awaitResult

class LoginPresenter(var accessable: Boolean, var view: LoginViewCallback) {
    private val appUtil = AppUtil()
    private val method = "auth.getMobileSession"

    fun authenticate(userName: String, password: String) = runBlocking {
        val params: MutableMap<String, String> = mutableMapOf()
        params["username"] = userName
        params["password"] = password
        params["method"] = method

        val apiSig: String = appUtil.generateApiSig(params)
        val client = Retrofit2LastFmClient.create(AuthMobileSessionService::class.java)
        val result: Result<MobileSessionApiResponse> = client.authenticate(userName, password, apiSig).awaitResult()
        when (result) {
            is Result.Ok -> {
                val mobileSession = result.value.mobileSession
                view.setSessionInfo(mobileSession.key, mobileSession.name)
            }
            is Result.Error -> {
            }
            is Result.Exception -> {

            }
        }
    }

    fun backToSettingsWhenLoggedIn(success: Boolean?, sessionKey: String) {
        if (success!! && sessionKey.isNotEmpty()) {
            if (accessable) {
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