package com.mataku.scrobscrob.app.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.mataku.scrobscrob.app.model.Scrobble
import com.mataku.scrobscrob.app.ui.view.ScrobbleView

class ScrobbleListAdapter(private val context: Context) : BaseAdapter() {
    lateinit var scrobbles: List<Scrobble>

    override fun getCount(): Int = scrobbles.size

    override fun getItem(position: Int): Any = scrobbles[position]

    override fun getItemId(p0: Int): Long = 0

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View =
            ((view as? ScrobbleView) ?: ScrobbleView(context)).apply {
                setScrobble(scrobbles[position])
            }
}