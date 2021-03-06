package com.mataku.scrobscrob.app.ui.top.scrobble

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.app.App
import com.mataku.scrobscrob.app.model.RxEventBus
import com.mataku.scrobscrob.app.util.SharedPreferencesHelper
import com.mataku.scrobscrob.core.entity.Track
import com.mataku.scrobscrob.core.entity.UpdateNowPlayingEvent
import com.mataku.scrobscrob.core.entity.UpdateScrobbledListEvent
import com.mataku.scrobscrob.databinding.FragmentScrobbleBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScrobbleFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: FragmentScrobbleBinding
    private lateinit var swipeRefreshLayout: androidx.swiperefreshlayout.widget.SwipeRefreshLayout
    private val job = Job()
    private val coroutineContext = job

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val scrobbleView = inflater.inflate(R.layout.fragment_scrobble, container, false)
        binding = FragmentScrobbleBinding.bind(scrobbleView)
        setUpSwipeRefreshView()
        setUpRecyclerView()
        setUpNowPlayingView(dummyTrack())

        GlobalScope.launch(Dispatchers.Main) {
            RxEventBus.asChannel<UpdateNowPlayingEvent>().onEach {
                setUpNowPlayingView(it.track)
            }

            RxEventBus.asChannel<UpdateScrobbledListEvent>().onEach {
                onRefresh()
            }
        }

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
            R.color.yellow
        )
        swipeRefreshLayout.setOnRefreshListener(this)
    }

    private fun setUpRecyclerView() {
        val scrobbleViewAdapter = ScrobbleViewAdapter()
        CoroutineScope(coroutineContext).launch {
            val result = async {
                val scrobbleRecyclerView = binding.scrobbleListView
                scrobbleRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
                scrobbleRecyclerView.hasFixedSize()
                if (scrobbleRecyclerView.adapter == null) {
                    scrobbleRecyclerView.adapter = scrobbleViewAdapter
                }
                val dao = App.database.scrobbleDao
                dao.getScrobbles(20, 0)
            }.await()
            withContext(Dispatchers.Main) {
                scrobbleViewAdapter.setScrobbles(result)
            }
        }
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

        if (context == null) {
            return notLoggedInTrack
        }

        context?.let {
            val sharedPreferencesHelper = SharedPreferencesHelper(it)
            if (sharedPreferencesHelper.getSessionKey().isEmpty()) {
                return notLoggedInTrack
            }
        }

        return Track(
            getString(R.string.label_not_playing_message),
            getString(R.string.label_now_playing),
            getString(R.string.label_not_playing)
        )
    }
}