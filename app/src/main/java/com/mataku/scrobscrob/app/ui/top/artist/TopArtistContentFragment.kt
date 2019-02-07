package com.mataku.scrobscrob.app.ui.top.artist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.app.model.entity.presentation.Result
import com.mataku.scrobscrob.app.ui.top.TopViewModel
import com.mataku.scrobscrob.databinding.FragmentTopArtistsBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TopArtistContentFragment : Fragment() {

    private lateinit var binding: FragmentTopArtistsBinding
    private lateinit var controller: TopArtistController
    private var currentPage = 1

    val topViewModel: TopViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_top_artists, null, false)
        context?.let {
            binding = FragmentTopArtistsBinding.bind(view)
            binding.lifecycleOwner = this
            binding.userTopArtistRecyclerView.setController(controller)
            val displayMetrics = it.resources.displayMetrics
            val halfWidth = displayMetrics.widthPixels / 2

            controller = TopArtistController(halfWidth)

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
        binding.userTopArtistRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
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
                        topViewModel.loadArtists(currentPage, userName)
                    }
                }
            }
        })
        topViewModel.topArtistsResult.observe(this, Observer {
            when (it) {
                is Result.Success -> {
                    controller.setArtists(it.data)
                }
            }
        })
        topViewModel.loadArtists(currentPage, userName)
    }
}