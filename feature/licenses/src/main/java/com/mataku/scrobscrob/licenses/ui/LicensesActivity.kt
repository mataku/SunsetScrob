package com.mataku.scrobscrob.licenses.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mataku.scrobscrob.licenses.R
import com.mataku.scrobscrob.licenses.databinding.ActivityLicensesBinding

class LicensesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityLicensesBinding = DataBindingUtil.setContentView(this, R.layout.activity_licenses)
        val webView = binding.licenses
        webView.loadUrl("file:///android_asset/licenses.html")
    }
}