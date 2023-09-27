package com.example.playlistmaker.ui.media_lib.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediaLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator

class MediaLibraryFragment : Fragment() {

    companion object {

        private const val ARGS_FEATURED_TRACKS = "featuredTracks"
        private const val ARGS_PLAYLISTS = "playlists"


        fun createArgs(tracks: String, playlists: String): Bundle =
            bundleOf(
                ARGS_FEATURED_TRACKS to tracks,
                ARGS_PLAYLISTS to playlists
            )
    }


    private lateinit var binding: FragmentMediaLibraryBinding

    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val featuredTracks = ""
        val playlists = ""

        binding.mediaLibViewPager.adapter = MediaLibraryViewPagerAdapter(
            childFragmentManager,
            lifecycle, featuredTracks, playlists
        )

        tabMediator =
            TabLayoutMediator(binding.mediaLibTab, binding.mediaLibViewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.featured_tracks)
                    1 -> tab.text = getString(R.string.playlists)
                }
            }
        tabMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }
}