package com.mataku.scrobscrob.app.presenter

import com.mataku.scrobscrob.app.ui.view.LoginViewCallback
import org.junit.Test
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.test.assertTrue
import kotlin.test.fail

class LoginPresenterTest {
    @Test
    @Throws(Exception::class)
    fun backToSettingsWhenLoggedIn_success() {
        val showSuccessMessageCalled = AtomicBoolean()

        class MockView : MockLoginView() {
            override fun showSuccessMessage() {
                showSuccessMessageCalled.set(true)
            }
        }

        val presenter = LoginPresenter(MockView())
        presenter.backToSettingsWhenLoggedIn(true, "someKey")
        assertTrue(showSuccessMessageCalled.get())
    }

    @Test
    @Throws
    fun backToSettingsWhenLoggedIn_failure() {
        val focusOnPasswordViewCalled = AtomicBoolean()

        class MockView : MockLoginView() {
            override fun focusOnPasswordView() {
                focusOnPasswordViewCalled.set(true)
            }
        }

        val presenter = LoginPresenter(MockView())
        presenter.backToSettingsWhenLoggedIn(true, "")
        assertTrue(focusOnPasswordViewCalled.get())
    }

    private open class MockLoginView : LoginViewCallback {
        override fun showSuccessMessage() {
            fail()
        }

        override fun setSessionInfo(sessionKey: String, userName: String) {
            fail()
        }

        override fun focusOnPasswordView() {
            fail()
        }
    }
}