package com.example.playlistmaker.ui.media_lib.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFeaturedTracksBinding

class FeaturedTracksFragment : Fragment() {

    companion object {
        private const val FEATURED_TRACKS = "featured_tracks"

        fun newInstance(featuredTracks: String) = FeaturedTracksFragment().apply {
            arguments = Bundle().apply {
                putString(FEATURED_TRACKS, featuredTracks)
            }
        }
    }

    private var binding: FragmentFeaturedTracksBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentFeaturedTracksBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun emptyFeatureTracks() {
        binding?.apply {
            trackRecyclerView.visibility = View.GONE
            emptyMediaLib.visibility = View.VISIBLE
            findNothingImg.visibility = View.VISIBLE
        }
    }
}