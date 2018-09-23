package com.mataku.scrobscrob.app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.databinding.ActivityLicensesBinding

class LicensesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityLicensesBinding = DataBindingUtil.setContentView(this, R.layout.activity_licenses)

        val webView = binding.licenses
        webView.loadUrl("file:///android_asset/licenses.html")
    }
}