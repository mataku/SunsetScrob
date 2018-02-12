package com.mataku.scrobscrob.app.ui.item

import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.app.model.Track
import com.mataku.scrobscrob.databinding.ViewNowPlayingBinding
import com.xwray.groupie.databinding.BindableItem

class NowPlayingItem constructor(private val track: Track) : BindableItem<ViewNowPlayingBinding>() {
    override fun getLayout(): Int {
        return R.layout.view_now_playing
    }

    override fun bind(viewBinding: ViewNowPlayingBinding, position: Int) {
        viewBinding.track = track
    }
}