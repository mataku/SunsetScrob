package com.mataku.scrobscrob.app.ui

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.app.model.Scrobble
import com.mataku.scrobscrob.app.presenter.MainPresenter
import com.mataku.scrobscrob.app.receiver.AppleMusicNotificationReceiver
import com.mataku.scrobscrob.app.ui.adapter.ScrobbleListAdapter
import com.mataku.scrobscrob.app.ui.view.MainViewCallback
import io.realm.Realm
import io.realm.Sort

class MainActivity : AppCompatActivity(), MainViewCallback {
    private var receiver = AppleMusicNotificationReceiver()
    private lateinit var mainPresenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("DATA", Context.MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        Realm.init(this)
        this.title = "Latest 20 scrobbles"
//        if (BuildConfig.DEBUG) {
//            val realm = Realm.getDefaultInstance()
//            realm.executeTransaction {
//                realm.deleteAll()
//            }
//        }
        setContentView(R.layout.activity_main)
//        mainPresenter = MainPresenter(this)
//        mainPresenter.showPreparationMenuIfNeeded(isEnabledReadNotification())
//        setContentView(R.layout.activity_main)
        val filter = IntentFilter()
        filter.addAction("AppleMusic")
        registerReceiver(receiver, filter)

        val scrobbleListAdapter = ScrobbleListAdapter(applicationContext)
        scrobbleListAdapter.scrobbles = Scrobble().getCurrentTracks()
        var scrobbleListView = findViewById(R.id.list_view) as ListView
        scrobbleListView.adapter = scrobbleListAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    //    右上のメニューボタン表示
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.list, menu)
        return true
    }

    //    メニューボタンのクリックイベントを定義
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.overflow_options -> {
                showSettings()
                return true
            }
            else -> {
                showSettings()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showNotificationAccessSettingMenu() {
        val intent = Intent()
        intent.action = Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
        startActivity(intent)
    }

    override fun showLoginActivity() {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun showSettings() {
        val intent = Intent(applicationContext, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun isEnabledReadNotification(): Boolean {
        val contentResolver = contentResolver
        val rawListeners = Settings.Secure.getString(contentResolver,
                "enabled_notification_listeners")
        if (rawListeners == null || rawListeners == "") {
            return false
        } else {
            val listeners = rawListeners.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            listeners
                    .filter { it.startsWith(packageName) }
                    .forEach { return true }
        }
        return false
    }

    // TODO: あとで消す
    private fun dummyScrobbles(): List<Scrobble> {
        var list = ArrayList<Scrobble>()

        var scrobble = Scrobble()
        scrobble.artistName = "PassCode"
        scrobble.trackName = "insanity"
        scrobble.albumName = "ZENITH"
        list.add(scrobble)
        list.add(scrobble)
        return list
    }

    fun getAll(): List<Scrobble> {
        var limit = 20
        var lowerLimit = 0
        val realm = Realm.getDefaultInstance()
        val scrobblesCount = realm.where(Scrobble::class.java).findAll().count()
        if (scrobblesCount < limit) {
            var limit = scrobblesCount
        } else {
            lowerLimit = scrobblesCount - limit + 1
        }

        val result = realm.where(Scrobble::class.java).between("id", lowerLimit, limit).findAllSortedAsync("id", Sort.DESCENDING)
        return result
    }
}
