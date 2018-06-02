package com.mataku.scrobscrob.app.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.app.model.entity.Album
import com.mataku.scrobscrob.app.presenter.UserContentPresenter
import com.mataku.scrobscrob.app.ui.controller.TopAlbumController
import com.mataku.scrobscrob.app.ui.view.UserContentViewCallback
import com.mataku.scrobscrob.databinding.FragmentTopAlbumsBinding

class TopAlbumContentFragment : androidx.fragment.app.Fragment(), UserContentViewCallback {

    private lateinit var binding: FragmentTopAlbumsBinding
    private val presenter = UserContentPresenter(this)
    private val controller = TopAlbumController()
    private val albums = mutableListOf<Album>()
    private var currentPage = 1
    private lateinit var userName: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_top_albums, null, false)
        binding = DataBindingUtil.bind(view)!!
        binding.topAlbumRecyclerView.setController(controller)
        val sharedPreferences = this.activity?.getSharedPreferences("DATA", Context.MODE_PRIVATE)
        sharedPreferences?.let {
            userName = it.getString("UserName", "")
            if (userName.isNotEmpty()) {
                setUp()
            }
        }
        return view
    }

    override fun show(albums: List<Album>) {
        this.albums.addAll(albums)
        controller.setAlbums(this.albums)
    }

    private fun setUp() {
        presenter.getTopAlbums(userName, currentPage)
        binding.topAlbumRecyclerView.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val userContentRecyclerView = binding.topAlbumRecyclerView

                val totalCount = userContentRecyclerView.adapter.itemCount
                val childCount = userContentRecyclerView.childCount

                val layoutManager = userContentRecyclerView.layoutManager as androidx.recyclerview.widget.GridLayoutManager
                val firstPosition = layoutManager.findFirstVisibleItemPosition()
                if (totalCount == childCount + firstPosition) {
                    currentPage++
                    presenter.getTopAlbums(userName, currentPage)
                }
            }
        })
    }
}