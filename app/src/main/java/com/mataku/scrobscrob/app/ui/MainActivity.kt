package com.mataku.scrobscrob.app.ui

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Settings
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.app.data.Migration
import com.mataku.scrobscrob.app.model.Scrobble
import com.mataku.scrobscrob.app.presenter.MainPresenter
import com.mataku.scrobscrob.app.receiver.AppleMusicNotificationReceiver
import com.mataku.scrobscrob.app.ui.adapter.ScrobbleRecyclerViewAdapter
import com.mataku.scrobscrob.app.ui.view.MainViewCallback
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmConfiguration
import io.realm.RealmResults

class MainActivity : AppCompatActivity(), MainViewCallback, SwipeRefreshLayout.OnRefreshListener {
    private var receiver = AppleMusicNotificationReceiver()
    private lateinit var mainPresenter: MainPresenter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var scrobbleRecyclerView: RecyclerView
    private lateinit var scrobbleViewAdapter: ScrobbleRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Realm.init(this)
        val builder = RealmConfiguration.Builder()
        builder.schemaVersion(1L).migration(Migration())
        val config = builder.build()
        Realm.setDefaultConfiguration(config)
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
        val filter = IntentFilter()
        filter.addAction("AppleMusic")
        registerReceiver(receiver, filter)

        setUpSwipeRefreshView()
        setUpRecyclerView()
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

    override fun onRefresh() {
        setUpRecyclerView()
        swipeRefreshLayout.isRefreshing = false
    }

    private fun showSettings() {
        val intent = Intent(applicationContext, SettingsActivity::class.java)
        startActivity(intent)
    }
    
    private fun setUpSwipeRefreshView() {
        swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout)
        swipeRefreshLayout.setColorSchemeResources(
                R.color.colorAccent,
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                R.color.yellow)
        swipeRefreshLayout.setOnRefreshListener(this)
    }

    private fun setUpRecyclerView() {
        var scrobbles = Scrobble().getCurrentTracks()
        scrobbleViewAdapter = ScrobbleRecyclerViewAdapter(applicationContext, scrobbles)
        scrobbleViewAdapter.notifyDataSetChanged()
        scrobbleRecyclerView = findViewById<RecyclerView>(R.id.scrobble_list_view)
        scrobbleRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        scrobbleRecyclerView.hasFixedSize()
        scrobbleRecyclerView.adapter = scrobbleViewAdapter
    }
}