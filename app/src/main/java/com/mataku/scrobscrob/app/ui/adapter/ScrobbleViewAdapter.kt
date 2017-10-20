package com.mataku.scrobscrob.app.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.app.model.Scrobble
import io.realm.RealmResults

class ScrobbleViewAdapter(context: Context, scrobbles: RealmResults<Scrobble>) : RecyclerView.Adapter<ScrobbleViewAdapter.ViewHolder>() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private val scrobbles = scrobbles
    private val context = context

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ScrobbleViewAdapter.ViewHolder {
        val v = layoutInflater.inflate(R.layout.view_scrobble, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = scrobbles.size

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val scrobble = scrobbles[position]
        holder!!.trackArtistView.text = scrobble.artistName
        holder.trackNameView.text = scrobble.trackName
        if (scrobble.artwork == "") {
            Glide.with(context).load(R.drawable.no_image).into(holder.artWorkView)
        } else {
            Glide.with(context).load(scrobble.artwork).into(holder.artWorkView)
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val artWorkView: ImageView by lazy {
            v.findViewById<ImageView>(R.id.art_work_view)
        }

        val trackNameView: TextView by lazy {
            v.findViewById<TextView>(R.id.track_name_view)
        }

        val trackArtistView: TextView by lazy {
            v.findViewById<TextView>(R.id.track_artist_view)
        }
    }
}