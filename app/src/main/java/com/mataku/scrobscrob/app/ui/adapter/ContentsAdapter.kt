package com.mataku.scrobscrob.app.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.mataku.scrobscrob.app.ui.fragment.ScrobbleFragment
import com.mataku.scrobscrob.app.ui.fragment.TopAlbumContentFragment
import com.mataku.scrobscrob.app.ui.fragment.TopArtistContentFragment

open class ContentsAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager), ViewPager.OnPageChangeListener {
    override fun getCount(): Int = 3

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                ScrobbleFragment()
            }
            1 -> {
                TopAlbumContentFragment()
            }
            else -> {
                TopArtistContentFragment()
            }
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {

    }

    companion object {
        const val SCROBBLE_POSITION = 0
        const val TOP_ALBUM_POSITION = 1
        const val TOP_ARTISTS_POSITION = 2
    }
}