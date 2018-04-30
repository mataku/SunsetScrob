package com.mataku.scrobscrob.app.ui.fragment

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.app.model.entity.Artist
import com.mataku.scrobscrob.app.presenter.TopArtistsPresenter
import com.mataku.scrobscrob.app.ui.controller.TopArtistController
import com.mataku.scrobscrob.app.ui.view.TopArtistsContentViewCallback
import com.mataku.scrobscrob.databinding.FragmentTopArtistsBinding

class TopArtistContentFragment : Fragment(), TopArtistsContentViewCallback {

    private lateinit var binding: FragmentTopArtistsBinding
    private val presenter = TopArtistsPresenter(this)
    private val controller = TopArtistController()
    private val artists = mutableListOf<Artist>()
    private var currentPage = 1
    private lateinit var userName: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_top_artists, null, false)
        binding = DataBindingUtil.bind(view)!!
        binding.userTopArtistRecyclerView.setController(controller)
        val sharedPreferences = this.activity?.getSharedPreferences("DATA", Context.MODE_PRIVATE)
        sharedPreferences?.let {
            userName = it.getString("UserName", "")
            if (userName.isNotEmpty()) {
                setUp()
            }
        }
        return view
    }

    override fun show(artists: List<Artist>) {
        this.artists.addAll(artists)
        controller.setArtists(this.artists)
    }

    private fun setUp() {
        presenter.getTopArtists(userName, currentPage)
        binding.userTopArtistRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val topArtistRecyclerView = binding.userTopArtistRecyclerView

                val totalCount = topArtistRecyclerView.adapter.itemCount
                val childCount = topArtistRecyclerView.childCount

                val layoutManager = topArtistRecyclerView.layoutManager as GridLayoutManager
                val firstPosition = layoutManager.findFirstVisibleItemPosition()
                if (totalCount == childCount + firstPosition) {
                    currentPage++
                    presenter.getTopArtists(userName, currentPage)
                }
            }
        })
    }
}