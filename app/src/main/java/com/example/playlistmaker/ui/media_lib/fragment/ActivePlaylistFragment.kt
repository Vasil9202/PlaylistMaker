package com.example.playlistmaker.ui.media_lib.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentActivePlaylistBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.media_lib.TrackBottomAdapter
import com.example.playlistmaker.ui.media_lib.TrackBottomItemClickListener
import com.example.playlistmaker.ui.media_lib.view_model.PlaylistViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.Locale


class ActivePlaylistFragment : Fragment() {
    private lateinit var playlist: Playlist
    private lateinit var binding: FragmentActivePlaylistBinding
    private val args: ActivePlaylistFragmentArgs by navArgs()
    private var bottomNavigationView: BottomNavigationView? = null
    private val viewModel by activityViewModel<PlaylistViewModel>()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private var isClickAllowed = true
    private val trackBottomAdapter =
        TrackBottomAdapter(object : TrackBottomItemClickListener {
            override fun onTrackClick(track: Track) {
                if (clickDebounce()) {
                    val action = ActivePlaylistFragmentDirections.actionActivePlaylistFragmentToPlayerActivity(track)
                    findNavController().navigate(action)
                }
            }
            override fun onLongClickListener(track: Track): Boolean {
                TrackShowDialog(track)
                return true
            }
        })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigationView?.visibility = View.GONE
        binding = FragmentActivePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlist = args.playlistArg ?: Playlist(name = "")
        binding.tracksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.tracksRecyclerView.adapter = trackBottomAdapter
        viewModel.getTracksById(playlist.tracksIdList)
        viewModel.tracksListStatus().observe(viewLifecycleOwner){tracks ->
            setPlaylist(playlist, tracks)
            trackBottomAdapter.tracks.clear()
            trackBottomAdapter.tracks.addAll(tracks)
            trackBottomAdapter.notifyDataSetChanged()
        }

        setBottomSheet()

        binding.menuButton.setOnClickListener{
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }

        binding.arrowBack.setOnClickListener{
            findNavController().navigateUp()
        }

        binding.shareText.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            playlistShare()
        }

        binding.editText.setOnClickListener {
            val action = ActivePlaylistFragmentDirections.actionActivePlaylistFragmentToEditPlaylistFragment(playlist)
            findNavController().navigate(action)
        }

        binding.deleteTrack.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            playlistShowDialog()
        }

        binding.shareButton.setOnClickListener{
            playlistShare()
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        bottomNavigationView?.visibility = View.VISIBLE
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            this.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }
    fun setPlaylist(playlist: Playlist, tracks: List<Track>) = with(binding){
        if(playlist.placeholderPath.isNotEmpty()){
            binding.placeholder.setImageURI(Uri.parse(playlist.placeholderPath))
        }
        name.text = playlist.name
        description.text = playlist.description
        time.text = StringBuilder().append(
            SimpleDateFormat(
                "mm",
                Locale.getDefault()
            ).format(tracks.sumOf { it.trackTimeMilliSec.toInt() })
        ).append(" минут")
        count.text = "${tracks.size} треков"
    }

    private fun TrackShowDialog(track: Track) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.track_delete) // Описание диалога
            .setNegativeButton(R.string.no) { _, _ ->
            }
            .setPositiveButton(R.string.yes) { _, _ -> // Добавляет кнопку «Да»
                viewModel.deleteTrack(playlist,track)
            }
            .show()
    }


    private fun playlistShowDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage("${getString(R.string.wanna_delete_playlist)} ${playlist.name}?") // Описание диалога
            .setNegativeButton(R.string.no) { dialog, which ->
            }
            .setPositiveButton(R.string.yes) { dialog, which -> // Добавляет кнопку «Да»
                viewModel.deletePlaylist(playlist)
                    findNavController().navigateUp()
            }
            .show()
    }

    private fun setBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.menuBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }
                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    private fun playlistShare(){
        if (playlist.tracksCount == 0){
            Toast.makeText(requireContext(), R.string.no_tracks, Toast.LENGTH_LONG).show()
        }
        else{
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(
                Intent.EXTRA_TEXT, "${playlist.name} \n ${playlist.description} \n" +
                        "${String.format("%02d", playlist.tracksCount)} треков \n" +
                        "${trackBottomAdapter.tracks.joinToString("\n") {track -> "${track.trackId}. " +
                                "${track.artistName} - ${track.trackName} " +
                                "(${track.trackTimeMinute()})"}}"
            )
            startActivity(Intent.createChooser(intent, getString(R.string.send_link_via)))
        }
    }
    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}