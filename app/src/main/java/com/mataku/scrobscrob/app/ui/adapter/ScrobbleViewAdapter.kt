package com.mataku.scrobscrob.app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.app.model.Scrobble
import com.mataku.scrobscrob.databinding.ViewScrobbleBinding
import io.realm.RealmResults

class ScrobbleViewAdapter(private val scrobbles: RealmResults<Scrobble>) : RecyclerView.Adapter<ScrobbleViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrobbleViewAdapter.ViewHolder {
        val binding = ViewScrobbleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = scrobbles.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val scrobble = scrobbles[position]
        scrobble?.let {
            holder.setScrobble(it)
        }
    }

    class ViewHolder(private val binding: ViewScrobbleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setScrobble(scrobble: Scrobble) {
            binding.trackNameView.text = scrobble.trackName
            binding.trackArtistView.text = scrobble.artistName
            when (scrobble.artwork) {
                "" -> Glide.with(binding.artWorkView.context).load(R.drawable.no_image).into(binding.artWorkView)
                else -> Glide.with(binding.artWorkView.context).load(scrobble.artwork).into(binding.artWorkView)
            }
        }
    }
}