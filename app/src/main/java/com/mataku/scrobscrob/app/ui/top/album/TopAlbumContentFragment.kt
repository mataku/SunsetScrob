package com.mataku.scrobscrob.app.ui.top.album

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.app.model.entity.Album
import com.mataku.scrobscrob.databinding.FragmentTopAlbumsBinding

class TopAlbumContentFragment : Fragment(),
    TopAlbumContentViewCallback {

    private lateinit var binding: FragmentTopAlbumsBinding
    private val presenter = TopAlbumsPresenter(this)
    private val albums = mutableListOf<Album>()
    private var currentPage = 1

    private lateinit var controller: TopAlbumController

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_top_albums, null, false)
        binding = FragmentTopAlbumsBinding.bind(view)
        controller = TopAlbumController(this.context)

        binding.topAlbumRecyclerView.setController(controller)
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

    override fun show(albums: List<Album>) {
        this.albums.addAll(albums)
        controller.setAlbums(this.albums)
    }

    private fun setUp(userName: String) {
        presenter.getTopAlbums(userName, currentPage)
        binding.topAlbumRecyclerView.addOnScrollListener(object :
            androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val userContentRecyclerView = binding.topAlbumRecyclerView

                val adapter = userContentRecyclerView.adapter
                adapter?.let {
                    val totalCount = it.itemCount
                    val childCount = userContentRecyclerView.childCount

                    val layoutManager =
                        userContentRecyclerView.layoutManager as androidx.recyclerview.widget.GridLayoutManager
                    val firstPosition = layoutManager.findFirstVisibleItemPosition()
                    if (totalCount == childCount + firstPosition) {
                        currentPage++
                        presenter.getTopAlbums(userName, currentPage)
                    }
                }
            }
        })
    }
}