package com.example.playlistmaker.ui.media_lib.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class MediaLibraryViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle,
                                              private val featuredTracks: String, private val playlists: String) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val itemCount = 2
    override fun getItemCount(): Int {
        return itemCount
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> FeaturedTracksFragment.newInstance(featuredTracks)
            else -> PlaylistsFragment.newInstance(playlists)
        }
    }
}