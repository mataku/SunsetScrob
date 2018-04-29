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
import com.mataku.scrobscrob.app.model.entity.Album
import com.mataku.scrobscrob.app.presenter.UserContentPresenter
import com.mataku.scrobscrob.app.ui.controller.TopAlbumController
import com.mataku.scrobscrob.app.ui.view.UserContentViewCallback
import com.mataku.scrobscrob.databinding.FragmentUserContentBinding

class TopAlbumContentFragment : Fragment(), UserContentViewCallback {

    private lateinit var binding: FragmentUserContentBinding
    private val presenter = UserContentPresenter(this)
    private val controller = TopAlbumController()
    private val albums = mutableListOf<Album>()
    private var currentPage = 1
    private lateinit var userName: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_user_content, null, false)
        binding = DataBindingUtil.bind(view)!!
        binding.userTopAlbumView.setController(controller)
        setUp()
        return view
    }

    override fun show(albums: List<Album>) {
        this.albums.addAll(albums)
        controller.setAlbums(this.albums)
    }

    private fun setUp() {
        if (this.context == null) return

        val sharedPreferences = this.activity?.getSharedPreferences("DATA", Context.MODE_PRIVATE)
        sharedPreferences?.let {
            userName = it.getString("UserName", "")

            presenter.getTopAlbums(userName, currentPage)
        }

        binding.userTopAlbumView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val userContentRecyclerView = binding.userTopAlbumView

                val totalCount = userContentRecyclerView.adapter.itemCount
                val childCount = userContentRecyclerView.childCount

                val layoutManager = userContentRecyclerView.layoutManager as GridLayoutManager
                val firstPosition = layoutManager.findFirstVisibleItemPosition()
                if (totalCount == childCount + firstPosition) {
                    currentPage++
                    presenter.getTopAlbums(userName, currentPage)
                }
            }
        })
    }
}