package com.mataku.scrobscrob.app.ui.top.scrobble

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.core.GlideApp
import com.mataku.scrobscrob.core.entity.Scrobble
import com.mataku.scrobscrob.databinding.ViewScrobbleBinding

class ScrobbleViewAdapter() : RecyclerView.Adapter<ScrobbleViewAdapter.ViewHolder>() {

    private var scrobbles: List<Scrobble> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewScrobbleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    fun setScrobbles(scrobbleList: List<Scrobble>) {
        scrobbles = scrobbleList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return scrobbles.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val scrobble = scrobbles[position]
        scrobble.let {
            holder.setScrobble(it)
        }
    }

    class ViewHolder(private val binding: ViewScrobbleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setScrobble(scrobble: Scrobble) {
            binding.trackNameView.text = scrobble.trackName
            binding.trackArtistView.text = scrobble.artistName
            when (scrobble.artwork) {
                "" -> GlideApp.with(binding.artWorkView.context).load(R.drawable.no_image).into(binding.artWorkView)
                else -> GlideApp.with(binding.artWorkView.context).load(scrobble.artwork).into(binding.artWorkView)
            }
        }
    }
}
