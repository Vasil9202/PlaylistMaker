package com.example.playlistmaker.ui.media_lib.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.ui.media_lib.PlaylistAdapter
import com.example.playlistmaker.ui.media_lib.PlaylistItemClickListener
import com.example.playlistmaker.ui.media_lib.view_model.PlaylistViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    companion object {
        private const val PLAYLIST_TRACKS = "playlists"
        private const val CLICK_DEBOUNCE_DELAY = 1000L

        fun newInstance(playlists: String) = PlaylistsFragment().apply {
            arguments = Bundle().apply {
                putString(PLAYLIST_TRACKS, playlists)
            }
        }
    }

    private lateinit var binding: FragmentPlaylistsBinding
    private var isClickAllowed = true
    private val viewModel by viewModel<PlaylistViewModel>()
    private val playlistAdapter = PlaylistAdapter(object : PlaylistItemClickListener {
        override fun onPlaylistClick(playlist: Playlist) {
            if (clickDebounce()) {
                val action = MediaLibraryFragmentDirections.actionMediaLibraryFragmentToActivePlaylistFragment(playlist)
                findNavController().navigate(action)
            }
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllPlaylists()

        binding.playlistRecyclerView.layoutManager = GridLayoutManager(requireContext(), /*Количество столбцов*/ 2) //ориентация по умолчанию — вертикальная
        binding.playlistRecyclerView.adapter = playlistAdapter

        viewModel.playlistsStatus().observe(viewLifecycleOwner){playlists ->
            if(playlists.isEmpty()){
                playlistsEmpty()
            }else{
                binding.playlistRecyclerView.visibility = View.VISIBLE
                playlistAdapter.playlists.clear()
                playlistAdapter.playlists.addAll(playlists)
                playlistAdapter.notifyDataSetChanged()
                binding.findNothingImg.visibility = View.GONE
                binding.findNothingText.visibility = View.GONE
            }
        }
        binding.playlistRecyclerView.visibility = View.VISIBLE


        binding.newPlaylistButton.setOnClickListener{
            val action =
                MediaLibraryFragmentDirections.actionMediaLibraryFragmentToNewPlaylistFragment()
            findNavController().navigate(action)
        }
    }

    override fun onResume() {
        super.onResume()
        isClickAllowed = true
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
    private fun playlistsEmpty(){
        binding.playlistRecyclerView.visibility = View.GONE
        binding.findNothingImg.visibility = View.VISIBLE
        binding.findNothingText.visibility = View.VISIBLE
    }
}