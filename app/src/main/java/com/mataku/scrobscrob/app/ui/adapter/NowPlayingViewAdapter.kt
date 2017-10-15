package com.mataku.scrobscrob.app.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.app.model.Track

class NowPlayingViewAdapter(context: Context, track: Track) : RecyclerView.Adapter<NowPlayingViewAdapter.ViewHolder>() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private val track = track

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): NowPlayingViewAdapter.ViewHolder {
        val v = layoutInflater.inflate(R.layout.view_now_playing, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val trackDetail = "${track.name} - ${track.albumName}"
        holder!!.trackTextView.text = trackDetail
        holder.artistTextView.text = track.artistName
    }

    override fun getItemCount(): Int = 1

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val trackTextView: TextView by lazy {
            v.findViewById<TextView>(R.id.view_now_playing_track)
        }

        val artistTextView: TextView by lazy {
            v.findViewById<TextView>(R.id.view_now_playing_artist)
        }
    }
}