package com.example.playlistmaker.ui.player.activity

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.media_lib.fragment.FragmentCommunicator
import com.example.playlistmaker.ui.media_lib.fragment.NewPlaylistFragment
import com.example.playlistmaker.ui.player.PlaylistBottomAdapter
import com.example.playlistmaker.ui.player.PlaylistBottomItemClickListener
import com.example.playlistmaker.ui.player.view_model.PlayerActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayerActivity : AppCompatActivity(), FragmentCommunicator {

    private lateinit var track: Track
    private lateinit var binding: ActivityPlayerBinding
    private val viewModel by viewModel<PlayerActivityViewModel>()
    private var isClickAllowed = true
    private val args: PlayerActivityArgs by navArgs()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private val playlistList: MutableList<Playlist> = mutableListOf()
    private val playlistBottomAdapter =
        PlaylistBottomAdapter(object : PlaylistBottomItemClickListener {
            override fun onPlaylistClick(playlists: Playlist) {
                if (clickDebounce()) {
                    if (playlists.tracksIdList.contains(track.trackId)) {
                        Toast.makeText(
                            this@PlayerActivity,
                            "Трек уже добавлен в плейлист ${playlists.name}",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        viewModel.addTrackToPlaylist(playlists, track)
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                        Toast.makeText(
                            this@PlayerActivity,
                            "Добавлено в плейлист ${playlists.name}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getPlaylists()
        binding.playlistsRecyclerView.layoutManager = LinearLayoutManager(this)
        setObservers()
        setBottomSheet()
        setClickListeners()
        binding.newPlaylistButton.setOnClickListener {
            if (savedInstanceState == null) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                supportFragmentManager.beginTransaction().add(
                    binding.newPlaylistFragmentContainerView.id,
                    NewPlaylistFragment(),
                    "NewPlaylistFrag"
                )
                    .commit()
            }
        }
        track = args.trackArg ?: EMPTY_TRACK
        viewModel.initMediaPlayer(track)
        downloadData()
    }

    override fun onPause() {
        super.onPause()
        binding.playButton.setImageResource(R.drawable.play);
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.releasePlayer()
    }

    private fun downloadData() {
        Glide.with(this)
            .load(track.getCoverArtwork())
            .centerCrop()
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.DP8)))
            .placeholder(R.drawable.cover)
            .into(binding.placeholder)

        if (track.isFavorite) {
            binding.likeButton.setImageResource(R.drawable.like_on)
        }
        binding.trackName.text = track.trackName
        binding.group.text = track.artistName
        binding.trackTime.text = track.trackTimeMinute()
        binding.trackAlbum.text = track.collectionName
        binding.trackYear.text = track.getReleaseYear()
        binding.trackGenre.text = track.primaryGenreName
        binding.trackCountry.text = track.country
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

    private fun setObservers() {
        viewModel.observePlaylistState().observe(this) {
            binding.playlistsRecyclerView.adapter = playlistBottomAdapter
            playlistList.clear()
            playlistBottomAdapter.playlists.clear()
            playlistList.addAll(it)
            playlistBottomAdapter.playlists.addAll(it)
            playlistBottomAdapter.notifyDataSetChanged()
        }
        viewModel.observePlayerState().observe(this) {
            binding.playButton.isEnabled = it.isPlayButtonEnabled
            if (it.buttonText == PLAY) {
                binding.playButton.setImageResource(R.drawable.play)
            } else if (it.buttonText == PAUSE) {
                binding.playButton.setImageResource(R.drawable.pause)
            }
            binding.currentTime.text = it.progress
        }
        viewModel.onFavoriteState().observe(this) { isFavourite ->
            if (isFavourite) {
                binding.likeButton.setImageResource(R.drawable.like_on)
            } else {
                binding.likeButton.setImageResource(R.drawable.like_off)
            }
        }
    }

    private fun setBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet).apply {
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

    private fun setClickListeners() {

        binding.buttonBack.setOnClickListener {
            finish()
        }
        binding.collection.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
        binding.playButton.setOnClickListener {
            viewModel.onPlayButtonClicked()
        }
        binding.likeButton.setOnClickListener {
            viewModel.onFavoriteClicked(track)
        }
    }

    companion object {
        private const val PLAY = "PLAY"
        private const val PAUSE = "PAUSE"
        private val EMPTY_TRACK = Track(
            "", "", "", "", "", "", "", "", "", ""
        )
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    override fun fragmentDetached() {
        viewModel.getPlaylists()
    }
}