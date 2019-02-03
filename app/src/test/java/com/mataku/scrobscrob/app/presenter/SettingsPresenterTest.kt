package com.mataku.scrobscrob.app.presenter

import com.mataku.scrobscrob.app.ui.settings.SettingsViewCallback
import org.junit.Test
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.test.assertTrue
import kotlin.test.fail

class SettingsPresenterTest {
    @Test
    @Throws(Exception::class)
    fun setMessageAccordingToUserStatus_MessageForNotLoggedInUser() {
        var setMessageToLogInCalled = AtomicBoolean()

        class MockView : MockSettingsView() {
            override fun setMessageToLogIn() {
                setMessageToLogInCalled.set(true)
            }
        }

        val presenter = SettingsPresenter(MockView())
        presenter.setMessageAccordingToUserStatus("")
        assertTrue(setMessageToLogInCalled.get())
    }

    @Test
    @Throws(Exception::class)
    fun setMessageAccordingToUserStatus_MessageForLoggedInUser() {
        var setMessageToLogOutCalled = AtomicBoolean()

        class MockView : MockSettingsView() {
            override fun setMessageToLogOut(loggedInUserName: String) {
                setMessageToLogOutCalled.set(true)
            }
        }

        val presenter = SettingsPresenter(MockView())
        presenter.setMessageAccordingToUserStatus("mataku")
        assertTrue(setMessageToLogOutCalled.get())
    }

    @Test
    @Throws(Exception::class)
    fun setDestinationAccordingToUserStatus_showMenuToLogIn() {
        var setDestinationToMenuToLogInCalled = AtomicBoolean()

        class MockView : MockSettingsView() {
            override fun setDestinationToMenuToLogIn() {
                setDestinationToMenuToLogInCalled.set(true)
            }
        }

        val presenter = SettingsPresenter(MockView())
        presenter.setDestinationAccordingToUserStatus("")
        assertTrue(setDestinationToMenuToLogInCalled.get())
    }

    @Test
    @Throws(Exception::class)
    fun setDestinationAccordingToUserStatus_showMenuToLogOut() {
        var setDestinationToMenuToLogOutCalled = AtomicBoolean()

        class MockView : MockSettingsView() {
            override fun setDestinationToMenuToLogOut() {
                setDestinationToMenuToLogOutCalled.set(true)
            }
        }

        val presenter = SettingsPresenter(MockView())
        presenter.setDestinationAccordingToUserStatus("mataku")
        assertTrue(setDestinationToMenuToLogOutCalled.get())
    }

    private open class MockSettingsView : SettingsViewCallback {
        override fun setDestinationToMenuToLogIn() {
            fail()
        }

        override fun setDestinationToMenuToLogOut() {
            fail()
        }

        override fun setMessageToLogIn() {
            fail()
        }

        override fun setMessageToLogOut(loggedInUserName: String) {
            fail()
        }
    }
}