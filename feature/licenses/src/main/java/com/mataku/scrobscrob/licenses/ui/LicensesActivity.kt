package com.mataku.scrobscrob.licenses.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mataku.scrobscrob.licenses.R
import com.mataku.scrobscrob.licenses.databinding.ActivityLicensesBinding

class LicensesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        val binding: ActivityLicensesBinding = DataBindingUtil.setContentView(this, R.layout.activity_licenses)
        binding.licenses.loadUrl("file:///android_asset/licenses.html")
    }
}