package com.mataku.scrobscrob.app.ui

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.app.model.Scrobble
import com.mataku.scrobscrob.app.presenter.MainPresenter
import com.mataku.scrobscrob.app.receiver.AppleMusicNotificationReceiver
import com.mataku.scrobscrob.app.ui.adapter.ScrobbleRecyclerViewAdapter
import com.mataku.scrobscrob.app.ui.view.MainViewCallback
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults

class MainActivity : AppCompatActivity(), MainViewCallback, SwipeRefreshLayout.OnRefreshListener {
    private var receiver = AppleMusicNotificationReceiver()
    private lateinit var mainPresenter: MainPresenter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var scrobbleRecyclerView: RecyclerView
    private lateinit var scrobbleViewAdapter: ScrobbleRecyclerViewAdapter

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

    override fun onRefresh() {
        swipeRefreshLayout.isRefreshing = false
    }

    private fun showSettings() {
        val intent = Intent(applicationContext, SettingsActivity::class.java)
        startActivity(intent)
    }

    // TODO: あとで消す
    private fun dummyScrobbles(): List<Scrobble> {
        var list = ArrayList<Scrobble>()

        var scrobble = Scrobble()
        scrobble.artistName = "PassCode"
        scrobble.trackName = "insanity"
        scrobble.albumName = "ZENITH"
        scrobble.artwork = "https://i0.wp.com/lh3.googleusercontent.com/-ruPaz8Mf6VE/VmRzdQDWxaI/AAAAAAAAMMM/szeLTfOEb6w/s0/maxresdefault.jpg"
        list.add(scrobble)
        list.add(scrobble)
        return list
    }

    private fun fetchCurrentScrobbles() {
        var scrobbles = Scrobble().getCurrentTracks()
        scrobbles.addChangeListener(object : RealmChangeListener<RealmResults<Scrobble>> {
            override fun onChange(t: RealmResults<Scrobble>?) {
                scrobbles.removeChangeListener(this)
                notifyToAdapter(scrobbles)
            }
        })
    }

    private fun notifyToAdapter(scrobble: RealmResults<Scrobble>) {
        val scrobbleViewAdapter = ScrobbleRecyclerViewAdapter(applicationContext, scrobble)
        val scrobbleRecyclerView = findViewById(R.id.scrobble_list_view) as RecyclerView
        scrobbleRecyclerView.adapter = scrobbleViewAdapter
    }

    private fun setUpSwipeRefreshView() {
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout) as SwipeRefreshLayout
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
        scrobbleRecyclerView = findViewById(R.id.scrobble_list_view) as RecyclerView
        scrobbleRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        scrobbleRecyclerView.hasFixedSize()
        scrobbleRecyclerView.adapter = scrobbleViewAdapter
    }
}
