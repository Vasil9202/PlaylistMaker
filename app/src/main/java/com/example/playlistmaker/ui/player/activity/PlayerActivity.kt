package com.example.playlistmaker.ui.player.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.player.view_model.PlayerActivityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

const val PLAY = "PLAY"
const val PAUSE = "PAUSE"
class PlayerActivity : AppCompatActivity() {

    private lateinit var track: Track
    private lateinit var binding: ActivityPlayerBinding
    private val viewModel by viewModel<PlayerActivityViewModel>()
    private val args: PlayerActivityArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = args.trackArg ?: Track(
            "", "", "", "",
            "", "", "", "", ""
        )

        viewModel.initMediaPlayer(track)


        binding.buttonBack.setOnClickListener {
            finish()
        }

        downloadData()

        binding.playButton.setOnClickListener {
            viewModel.onPlayButtonClicked()
        }

        viewModel.observePlayerState().observe(this) {
            binding.playButton.isEnabled = it.isPlayButtonEnabled
            if(it.buttonText == PLAY)
                binding.playButton.setImageResource(R.drawable.play);
            else if(it.buttonText == PAUSE)
                binding.playButton.setImageResource(R.drawable.pause)
            binding.currentTime.text = it.progress
        }
    }

    override fun onPause() {
        super.onPause()
        binding.playButton.setImageResource(R.drawable.play);
        viewModel.pausePlayer()
    }

    override fun onResume() {
        super.onResume()

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

        binding.trackName.text = track.trackName
        binding.group.text = track.artistName
        binding.trackTime.text = track.trackTimeMin
        binding.trackAlbum.text = track.collectionName
        binding.trackYear.text = track.getReleaseYear()
        binding.trackGenre.text = track.primaryGenreName
        binding.trackCountry.text = track.country
    }


}