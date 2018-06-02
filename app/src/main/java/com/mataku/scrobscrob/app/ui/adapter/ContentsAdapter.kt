package com.mataku.scrobscrob.app.ui.adapter

import com.mataku.scrobscrob.app.ui.fragment.ScrobbleFragment
import com.mataku.scrobscrob.app.ui.fragment.TopAlbumContentFragment
import com.mataku.scrobscrob.app.ui.fragment.TopArtistContentFragment

open class ContentsAdapter(fragmentManager: androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(fragmentManager), androidx.viewpager.widget.ViewPager.OnPageChangeListener {
    override fun getCount(): Int = 3

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
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
}