package com.mataku.scrobscrob.app.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
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
}