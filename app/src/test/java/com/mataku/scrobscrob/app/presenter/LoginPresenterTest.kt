package com.mataku.scrobscrob.app.presenter

import com.mataku.scrobscrob.app.ui.login.LoginPresenter
import com.mataku.scrobscrob.app.ui.login.LoginViewCallback
import com.mataku.scrobscrob.core.api.repository.MobileSessionRepository
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.mockito.Mockito
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.test.assertTrue
import kotlin.test.fail

@RunWith(JUnitPlatform::class)
class LoginPresenterTest : Spek({

    val mockRepo = Mockito.mock(MobileSessionRepository::class.java)

    describe("backToSettingsWhenLoggedIn") {

        context("Logged in user") {
            it("Call successful callbacks") {
                val showSuccessMessageCalled = AtomicBoolean()
                val backToSettingsActivityCalled = AtomicBoolean()

                class MockView : MockLoginView() {
                    override fun showSuccessMessage() {
                        showSuccessMessageCalled.set(true)
                    }

                    override fun backToSettingsActivity() {
                        backToSettingsActivityCalled.set(true)
                    }
                }

                val presenter = LoginPresenter(true, MockView(), mockRepo)
                presenter.backToSettingsWhenLoggedIn(true, "someKey")
                assertTrue(showSuccessMessageCalled.get())
                assertTrue(backToSettingsActivityCalled.get())
            }
        }

        context("No keys") {
            it("Focus on password view") {
                val focusOnPasswordViewCalled = AtomicBoolean()

                class MockView : MockLoginView() {
                    override fun focusOnPasswordView() {
                        focusOnPasswordViewCalled.set(true)
                    }
                }

                val presenter = LoginPresenter(true, MockView(), mockRepo)
                presenter.backToSettingsWhenLoggedIn(true, "")
                assertTrue(focusOnPasswordViewCalled.get())
            }
        }
    }
})

open class MockLoginView : LoginViewCallback {
    override fun showSuccessMessage() {
        fail()
    }

    override fun setSessionInfo(sessionKey: String, userName: String) {
        fail()
    }

    override fun focusOnPasswordView() {
        fail()
    }

    override fun backToSettingsActivity() {
        fail()
    }

    override fun showMessageToAllowAccessToNotification() {
        fail()
    }

    override fun showError() {
        fail()
    }
}