package com.example.playlistmaker.ui.media_lib.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.FragmentFeaturedTracksBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.media_lib.view_model.FeaturedTracksViewModel
import com.example.playlistmaker.ui.search.ItemClickListener
import com.example.playlistmaker.ui.search.TrackAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeaturedTracksFragment : Fragment() {

    companion object {
        private const val FEATURED_TRACKS = "featured_tracks"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        fun newInstance(featuredTracks: String) = FeaturedTracksFragment().apply {
            arguments = Bundle().apply {
                putString(FEATURED_TRACKS, featuredTracks)
            }
        }
    }

    private lateinit var binding: FragmentFeaturedTracksBinding
    private var isClickAllowed = true
    private val viewModel by viewModel<FeaturedTracksViewModel>()
    private val adapter = TrackAdapter(object : ItemClickListener {
        override fun onTrackClick(track: Track) {
            if (clickDebounce()) {
                val action =
                    MediaLibraryFragmentDirections.actionMediaLibraryFragmentToPlayerActivity(track)
                findNavController().navigate(action)
            }
        }
    })


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeaturedTracksBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.trackRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.trackRecyclerView.adapter = adapter
        viewModel.isFavouriteTracksEmpty().observe(viewLifecycleOwner){
           if(it){ emptyFeatureTracks()}
        }
        viewModel.getFavouriteTracks().observe(viewLifecycleOwner) {
                showContent(it)
        }
    }

    private fun emptyFeatureTracks() {
        binding.apply {
            trackRecyclerView.visibility = View.GONE
            emptyMediaLib.visibility = View.VISIBLE
            findNothingImg.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateFavouriteTracks()
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun showContent(contentTracks: List<Track>) {

        adapter.tracks.clear()
        adapter.tracks.addAll(contentTracks)
        adapter.notifyDataSetChanged()
        binding.apply {
            emptyMediaLib.visibility = View.GONE
            findNothingImg.visibility = View.GONE
            trackRecyclerView.visibility = View.VISIBLE
        }


    }
}