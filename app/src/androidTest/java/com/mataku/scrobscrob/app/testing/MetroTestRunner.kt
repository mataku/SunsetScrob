package com.mataku.scrobscrob.app.testing

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class MetroTestRunner : AndroidJUnitRunner() {
  override fun newApplication(
    cl: ClassLoader?,
    className: String?,
    context: Context?,
  ): Application = super.newApplication(cl, TestApp::class.java.name, context)
}
