package com.mataku.scrobscrob.app.ui.top

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.app.receiver.AppleMusicNotificationReceiver
import com.mataku.scrobscrob.app.ui.screen.MainScreen
import com.mataku.scrobscrob.app.ui.viewmodel.MainViewModel
import com.mataku.scrobscrob.app.util.SharedPreferencesHelper
import com.mataku.scrobscrob.data.repository.UsernameRepository
import com.mataku.scrobscrob.ui_common.style.SunsetTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var receiver = AppleMusicNotificationReceiver()
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    @Inject
    lateinit var usernameRepository: UsernameRepository

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        setTheme(R.style.AppTheme)

        lifecycleScope.launch {
            viewModel.theme.collect {
                it?.let { theme ->
                    setContent {
                        SunsetTheme(theme = theme) {
                            MainScreen(
                                usernameRepository.username()
                            )
                        }
                    }
                }
            }
        }

//        sharedPreferencesHelper = SharedPreferencesHelper(this)
//        this.title = "Latest 20 scrobbles (Beta)"
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
//        val filter = IntentFilter()
//        filter.addAction("AppleMusic")
//        registerReceiver(receiver, filter)
//        setUpContentTab()
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        unregisterReceiver(receiver)
//    }
//
//    //    右上のメニューボタン表示
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.list, menu)
//        return true
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        when (requestCode) {
//            SETTINGS_REQUEST_CODE -> {
//                setUpContentTab()
//            }
//        }
//    }
//
//    //    メニューボタンのクリックイベントを定義
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.overflow_options -> {
//                showSettings()
//                true
//            }
//            else -> {
//                showSettings()
//                true
//            }
//        }
//    }
//
//    override fun showNotificationAccessSettingMenu() {
//        val intent = Intent()
//        intent.action = Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
//        startActivity(intent)
//    }
//
//    override fun showLoginActivity() {
//        val intent = Intent(applicationContext, LoginActivity::class.java)
//        startActivity(intent)
//    }
//
//    private fun showSettings() {
//        val intent = Intent(applicationContext, SettingsActivity::class.java)
//        startActivityForResult(intent, SETTINGS_REQUEST_CODE)
//    }
//
//    private fun setUpContentTab() {
//        val adapter = object : ContentsAdapter(supportFragmentManager) {
//            override fun onPageSelected(position: Int) {
//                when (position) {
//                    ContentsAdapter.SCROBBLE_POSITION -> {
////                        self.supportActionBar?.show()
//                        binding.fab.hide()
//                        self.title = "Latest 20 scrobbles (Beta)"
//                    }
//                    ContentsAdapter.TOP_ALBUM_POSITION -> {
////                        self.supportActionBar?.hide()
//                        binding.fab.hide()
//                        self.title = "Top Albums"
//                    }
//                    else -> {
//                        binding.fab.hide()
//                        self.title = "Top Artists"
//                    }
//                }
//            }
//
//            override fun onPageScrollStateChanged(state: Int) {
//            }
//        }
//
//        val viewPager = binding.activityMainViewpager
//        viewPager.also {
//            it.adapter = adapter
//            it.addOnPageChangeListener(adapter)
//            it.offscreenPageLimit = 2
//        }
//        val navItemSelectedListener =
//            BottomNavigationView.OnNavigationItemSelectedListener { item ->
//                when (item.itemId) {
//                    R.id.menu_scrobble -> {
//                        viewPager.currentItem = ContentsAdapter.SCROBBLE_POSITION
//                        return@OnNavigationItemSelectedListener true
//                    }
//
//                    R.id.menu_top_albums -> {
//                        viewPager.currentItem = ContentsAdapter.TOP_ALBUM_POSITION
//                        return@OnNavigationItemSelectedListener true
//                    }
//
//                    R.id.menu_top_artists -> {
//                        viewPager.currentItem = ContentsAdapter.TOP_ARTISTS_POSITION
//                        return@OnNavigationItemSelectedListener true
//                    }
//                }
//                false
//            }
//        binding.activityMainTablayout.setOnNavigationItemSelectedListener(navItemSelectedListener)
//    }
//
//    companion object {
//        const val SETTINGS_REQUEST_CODE = 1001
//    }
}