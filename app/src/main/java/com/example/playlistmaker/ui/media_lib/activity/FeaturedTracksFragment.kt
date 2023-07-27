package com.example.playlistmaker.ui.media_lib.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFeaturedTracksBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FeaturedTracksFragment : Fragment() {

    companion object {
        private const val FUETURED_TRACKS = "featured_tracks"

        fun newInstance(featuredTracks: String) = FeaturedTracksFragment().apply {
            arguments = Bundle().apply {
                putString(FUETURED_TRACKS, featuredTracks)
            }
        }
    }

    private lateinit var binding: FragmentFeaturedTracksBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentFeaturedTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun emptyFeatureTracks() {
        binding.apply {
            trackRecyclerView.visibility = View.GONE
            emptyMediaLib.visibility = View.VISIBLE
            findNothingImg.visibility = View.VISIBLE
        }
    }
}