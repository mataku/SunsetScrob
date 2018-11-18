package com.mataku.scrobscrob.app.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.app.model.entity.Artist
import com.mataku.scrobscrob.app.presenter.TopArtistsPresenter
import com.mataku.scrobscrob.app.ui.controller.TopArtistController
import com.mataku.scrobscrob.app.ui.view.TopArtistsContentViewCallback
import com.mataku.scrobscrob.databinding.FragmentTopArtistsBinding

class TopArtistContentFragment : Fragment(), TopArtistsContentViewCallback {

    private lateinit var binding: FragmentTopArtistsBinding
    private val presenter = TopArtistsPresenter(this)
    private lateinit var controller: TopArtistController
    private val artists = mutableListOf<Artist>()
    private var currentPage = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_top_artists, null, false)
        controller = TopArtistController(context)
        binding = FragmentTopArtistsBinding.bind(view)
        binding.userTopArtistRecyclerView.setController(controller)
        val sharedPreferences = this.activity?.getSharedPreferences("DATA", Context.MODE_PRIVATE)
        sharedPreferences?.let {

            val userName = it.getString("UserName", "")
            userName?.let { name ->
                if (name.isNotEmpty()) {
                    setUp(name)
                }
            }
        }
        return view
    }

    override fun show(artists: List<Artist>) {
        this.artists.addAll(artists)
        controller.setArtists(this.artists)
    }

    private fun setUp(userName: String) {
        presenter.getTopArtists(userName, currentPage)
        binding.userTopArtistRecyclerView.addOnScrollListener(object :
            androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val topArtistRecyclerView = binding.userTopArtistRecyclerView

                val adapter = topArtistRecyclerView.adapter

                adapter?.let {
                    val totalCount = it.itemCount
                    val childCount = topArtistRecyclerView.childCount

                    val layoutManager =
                        topArtistRecyclerView.layoutManager as androidx.recyclerview.widget.GridLayoutManager
                    val firstPosition = layoutManager.findFirstVisibleItemPosition()
                    if (totalCount == childCount + firstPosition) {
                        currentPage++
                        presenter.getTopArtists(userName, currentPage)
                    }
                }
            }
        })
    }
}