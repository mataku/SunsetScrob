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
import android.view.MotionEvent
import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.app.data.Migration
import com.mataku.scrobscrob.app.model.Scrobble
import com.mataku.scrobscrob.app.model.Track
import com.mataku.scrobscrob.app.presenter.MainPresenter
import com.mataku.scrobscrob.app.receiver.AppleMusicNotificationReceiver
import com.mataku.scrobscrob.app.ui.adapter.NowPlayingViewAdapter
import com.mataku.scrobscrob.app.ui.adapter.ScrobbleViewAdapter
import com.mataku.scrobscrob.app.ui.view.MainViewCallback
import io.realm.Realm
import io.realm.RealmConfiguration


class MainActivity : AppCompatActivity(), MainViewCallback, SwipeRefreshLayout.OnRefreshListener {
    private var receiver = AppleMusicNotificationReceiver()
    private lateinit var mainPresenter: MainPresenter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var scrobbleRecyclerView: RecyclerView
    private lateinit var nowPlayingView: RecyclerView
    private lateinit var scrobbleViewAdapter: ScrobbleViewAdapter
    private lateinit var nowPlayingViewAdapter: NowPlayingViewAdapter

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
        setUpNowPlayingView()
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
        val scrobbles = Scrobble().getCurrentTracks()
        scrobbleViewAdapter = ScrobbleViewAdapter(applicationContext, scrobbles)
        scrobbleViewAdapter.notifyDataSetChanged()
        scrobbleRecyclerView = findViewById<RecyclerView>(R.id.scrobble_list_view)
        scrobbleRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        scrobbleRecyclerView.hasFixedSize()
        scrobbleRecyclerView.adapter = scrobbleViewAdapter
    }

    private fun setUpNowPlayingView() {
        val track = Track("PassCode", "Zenith", "Zenith")
        nowPlayingViewAdapter = NowPlayingViewAdapter(applicationContext, track)
        nowPlayingView = findViewById(R.id.now_playing_view)
        nowPlayingView.layoutManager = LinearLayoutManager(applicationContext)
        nowPlayingView.hasFixedSize()
        nowPlayingView.addOnItemTouchListener(ScrollController())
        nowPlayingView.adapter = nowPlayingViewAdapter
    }


    inner class ScrollController : RecyclerView.OnItemTouchListener {

        override fun onInterceptTouchEvent(view: RecyclerView, event: MotionEvent): Boolean {
            return true
        }

        override fun onTouchEvent(view: RecyclerView, event: MotionEvent) {}

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
    }
}