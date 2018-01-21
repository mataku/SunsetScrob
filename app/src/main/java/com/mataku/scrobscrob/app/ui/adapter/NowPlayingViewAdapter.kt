package com.mataku.scrobscrob.app.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mataku.scrobscrob.app.model.Track
import com.mataku.scrobscrob.databinding.ViewNowPlayingBinding

class NowPlayingViewAdapter(private val track: Track) : RecyclerView.Adapter<NowPlayingViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): NowPlayingViewAdapter.ViewHolder? {
        parent ?: return null
        val binding = ViewNowPlayingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.setTrack(track)
    }

    override fun getItemCount(): Int = 1

    class ViewHolder(private val binding: ViewNowPlayingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setTrack(track: Track) {
            val trackDetail = "${track.name} - ${track.albumName}"
            binding.viewNowPlayingTrack.text = trackDetail
            binding.viewNowPlayingArtist.text = track.artistName
        }
    }
}