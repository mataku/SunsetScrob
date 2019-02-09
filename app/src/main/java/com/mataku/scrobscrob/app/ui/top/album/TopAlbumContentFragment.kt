package com.mataku.scrobscrob.app.ui.top.album

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.app.model.entity.presentation.Result
import com.mataku.scrobscrob.app.ui.top.TopViewModel
import com.mataku.scrobscrob.databinding.FragmentTopAlbumsBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TopAlbumContentFragment : Fragment() {

    private lateinit var binding: FragmentTopAlbumsBinding
    private var currentPage = 1

    val topViewModel: TopViewModel by sharedViewModel()

    private lateinit var controller: TopAlbumController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_top_albums, null, false)
        binding = FragmentTopAlbumsBinding.bind(view)
        binding.lifecycleOwner = this
        context?.let {
            val displayMetrics = it.resources.displayMetrics
            val halfWidth = displayMetrics.widthPixels / 2
            controller = TopAlbumController(halfWidth)

            binding.topAlbumRecyclerView.setController(controller)
            val sharedPreferences = this.activity?.getSharedPreferences("DATA", Context.MODE_PRIVATE)
            sharedPreferences?.let { sharedPref ->
                val userName = sharedPref.getString("UserName", "")
                userName?.let { name ->
                    if (name.isNotEmpty()) {
                        setUp(name)
                    }
                }
            }
        }
        return view
    }

    private fun setUp(userName: String) {
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
                        topViewModel.loadAlbums(currentPage, userName)
                    }
                }
            }
        })
        topViewModel.topAlbumsResult.observe(this, Observer {
            when (it) {
                is Result.Success -> {
                    controller.setAlbums(it.data)
                }
            }
        })
        topViewModel.loadAlbums(currentPage, userName)
    }
}