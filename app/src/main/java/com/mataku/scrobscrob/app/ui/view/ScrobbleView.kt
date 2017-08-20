package com.mataku.scrobscrob.app.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.app.model.Scrobble

class ScrobbleView : FrameLayout {
    constructor(context: Context?) : super(context)

    constructor(
            context: Context?,
            attrs: AttributeSet?
    ) : super(context, attrs)

    constructor(
            context: Context?,
            attrs: AttributeSet?,
            defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    constructor(
            context: Context?,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    val artWorkView: ImageView by lazy {
        findViewById(R.id.art_work_view) as ImageView
    }

    val trackNameView: TextView by lazy {
        findViewById(R.id.track_name_view) as TextView
    }

    val trackArtistView: TextView by lazy {
        findViewById(R.id.track_artist_view) as TextView
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_scrobble, this)
    }

    fun setScrobble(scrobble: Scrobble) {
        trackArtistView?.text = scrobble.artistName
        trackNameView?.text = scrobble.trackName
        if (scrobble.artwork.isNotEmpty()) {
            Glide.with(context).load(scrobble.artwork).into(artWorkView)
        }
    }
}