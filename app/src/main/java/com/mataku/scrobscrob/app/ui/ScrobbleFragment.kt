package com.mataku.scrobscrob.app.ui.view

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.app.model.Scrobble
import com.mataku.scrobscrob.app.model.Track
import com.mataku.scrobscrob.app.model.entity.RxEventBus
import com.mataku.scrobscrob.app.model.entity.UpdateNowPlayingEvent
import com.mataku.scrobscrob.app.model.entity.UpdateScrobbledListEvent
import com.mataku.scrobscrob.app.ui.adapter.NowPlayingViewAdapter
import com.mataku.scrobscrob.app.ui.adapter.ScrobbleViewAdapter
import com.mataku.scrobscrob.app.util.SharedPreferencesHelper
import com.mataku.scrobscrob.databinding.FragmentScrobbleBinding

class ScrobbleFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: FragmentScrobbleBinding
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val scrobbleView = inflater.inflate(R.layout.fragment_scrobble, container, false)
        binding = DataBindingUtil.bind(scrobbleView)
        setUpSwipeRefreshView()
        setUpRecyclerView()
        setUpNowPlayingView(dummyTrack())

        RxEventBus.stream(UpdateNowPlayingEvent::class.java).subscribe({
            setUpNowPlayingView(it.track)
        })

        RxEventBus.stream(UpdateScrobbledListEvent::class.java).subscribe({
            onRefresh()
        })

        return scrobbleView
    }

    override fun onRefresh() {
        setUpRecyclerView()
        swipeRefreshLayout.isRefreshing = false
    }


    private fun setUpSwipeRefreshView() {
        swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setColorSchemeResources(
                R.color.colorAccent,
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                R.color.yellow)
        swipeRefreshLayout.setOnRefreshListener(this)
    }

    private fun setUpRecyclerView() {
        val scrobbles = Scrobble().getCurrentTracks()
        val scrobbleViewAdapter = ScrobbleViewAdapter(scrobbles)
        scrobbleViewAdapter.notifyDataSetChanged()
        val scrobbleRecyclerView = binding.scrobbleListView
        scrobbleRecyclerView.layoutManager = LinearLayoutManager(context)
        scrobbleRecyclerView.hasFixedSize()
        scrobbleRecyclerView.adapter = scrobbleViewAdapter
    }

    private fun setUpNowPlayingView(track: Track) {
        val nowPlayingViewAdapter = NowPlayingViewAdapter(track)
        val nowPlayingView = binding.nowPlayingView
        nowPlayingView.layoutManager = LinearLayoutManager(context)
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

    private fun dummyTrack(): Track {
        val notLoggedInTrack = Track(
                getString(R.string.label_message_to_log_in),
                getString(R.string.label_now_playing),
                getString(R.string.label_not_logged_in)
        )

        context ?: return notLoggedInTrack
        val sharedPreferencesHelper = SharedPreferencesHelper(context!!)
        if (sharedPreferencesHelper.getSessionKey().isEmpty()) {
            return notLoggedInTrack
        }

        return Track(
                getString(R.string.label_not_playing_message),
                getString(R.string.label_now_playing),
                getString(R.string.label_not_playing)
        )
    }


}