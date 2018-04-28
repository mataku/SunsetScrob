package com.mataku.scrobscrob.app.ui.fragment

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.app.model.entity.Album
import com.mataku.scrobscrob.app.presenter.UserContentPresenter
import com.mataku.scrobscrob.app.ui.controller.TopAlbumController
import com.mataku.scrobscrob.app.ui.view.UserContentViewCallback
import com.mataku.scrobscrob.databinding.FragmentUserContentBinding

class UserContentFragment : Fragment(), UserContentViewCallback {

    private lateinit var binding: FragmentUserContentBinding
    private val presenter = UserContentPresenter(this)
    private val controller = TopAlbumController()
    private val albums = mutableListOf<Album>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_user_content, null, false)
        binding = DataBindingUtil.bind(view)!!
        binding.userTopAlbumView.setController(controller)
        presenter.getTopAlbums("rhythnn", 1)
        return view
    }

    override fun show(albums: List<Album>) {
        controller.setAlbums(albums)
    }
}