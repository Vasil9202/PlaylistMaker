package com.example.playlistmaker.ui.media_lib.activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class MediaLibraryViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle,
                                              private val featuredTracks: String, private val playlists: String) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> FeaturedTracksFragment.newInstance(featuredTracks)
            else -> PlaylistsFragment.newInstance(playlists)
        }
    }
}