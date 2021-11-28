package com.mataku.scrobscrob.licenses.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.mataku.scrobscrob.ui_common.template.WebViewScreen

class LicensesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WebViewScreen(url = LICENSES_HTML_FILEPATH)
        }
    }

    companion object {
        private const val LICENSES_HTML_FILEPATH = "file:///android_asset/licenses.html"
    }
}