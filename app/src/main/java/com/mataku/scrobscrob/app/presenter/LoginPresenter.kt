package com.mataku.scrobscrob.app.presenter

import com.mataku.scrobscrob.app.model.api.Retrofit2LastFmClient
import com.mataku.scrobscrob.app.model.api.service.AuthMobileSessionService
import com.mataku.scrobscrob.app.model.entity.MobileSessionApiResponse
import com.mataku.scrobscrob.app.ui.view.LoginViewCallback
import com.mataku.scrobscrob.app.util.AppUtil
import kotlinx.coroutines.experimental.runBlocking
import retrofit2.HttpException
import retrofit2.Response
import ru.gildor.coroutines.retrofit.awaitResponse

class LoginPresenter(var accessable: Boolean, var view: LoginViewCallback) {
    private val appUtil = AppUtil()
    private val method = "auth.getMobileSession"

    fun authenticate(userName: String, password: String) = runBlocking {
        var params: MutableMap<String, String> = mutableMapOf()
        params["username"] = userName
        params["password"] = password
        params["method"] = method
        params["api_key"] = appUtil.apiKey

        val apiSig: String = appUtil.generateApiSig(params)
        val client = Retrofit2LastFmClient.create(AuthMobileSessionService::class.java)
        try {
            val apiResponse: Response<MobileSessionApiResponse> = client.authenticate(userName, password, appUtil.apiKey, apiSig).awaitResponse()
            if (apiResponse.isSuccessful) {
                val mobileSession = apiResponse.body()!!.mobileSession
                view.setSessionInfo(mobileSession.key, mobileSession.name)
            }
        } catch (e: HttpException) {
            // e.printStackTrace()
        } catch (e: Throwable) {
            // e.printStackTrace()
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