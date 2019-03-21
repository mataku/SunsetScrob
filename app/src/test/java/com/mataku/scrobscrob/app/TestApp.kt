package com.mataku.scrobscrob.app

import org.robolectric.TestLifecycleApplication
import java.lang.reflect.Method
import kotlin.test.fail

class TestApp : App(), TestLifecycleApplication {
    override fun beforeTest(method: Method?) {
        Thread.currentThread().setUncaughtExceptionHandler { _, _ -> fail() }
    }

    override fun afterTest(method: Method?) {

    }

    override fun prepareTest(test: Any?) {

    }

    override fun onCreate() {
        
    }
}