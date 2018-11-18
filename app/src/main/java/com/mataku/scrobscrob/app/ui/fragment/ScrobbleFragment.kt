package com.mataku.scrobscrob.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.app.model.Scrobble
import com.mataku.scrobscrob.app.model.Track
import com.mataku.scrobscrob.app.model.entity.RxEventBus
import com.mataku.scrobscrob.app.model.entity.UpdateNowPlayingEvent
import com.mataku.scrobscrob.app.model.entity.UpdateScrobbledListEvent
import com.mataku.scrobscrob.app.ui.adapter.ScrobbleViewAdapter
import com.mataku.scrobscrob.app.util.SharedPreferencesHelper
import com.mataku.scrobscrob.databinding.FragmentScrobbleBinding

class ScrobbleFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: FragmentScrobbleBinding
    private lateinit var swipeRefreshLayout: androidx.swiperefreshlayout.widget.SwipeRefreshLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val scrobbleView = inflater.inflate(R.layout.fragment_scrobble, container, false)
        binding = DataBindingUtil.bind(scrobbleView)!!
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
        val scrobbleViewAdapter = ScrobbleViewAdapter()
        val scrobbleRecyclerView = binding.scrobbleListView
        scrobbleRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        scrobbleRecyclerView.hasFixedSize()
        if (scrobbleRecyclerView.adapter == null) {
            scrobbleRecyclerView.adapter = scrobbleViewAdapter
        }
        scrobbleViewAdapter.setScrobbles(scrobbles)
    }

    private fun setUpNowPlayingView(track: Track) {
        binding.viewNowPlayingTrack.text = track.name
        binding.viewNowPlayingArtist.text = track.artistName
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