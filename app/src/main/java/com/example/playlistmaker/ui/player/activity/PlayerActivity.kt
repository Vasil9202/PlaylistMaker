package com.example.playlistmaker.ui.player.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.player.view_model.PlayerActivityViewModel
import com.example.playlistmaker.ui.search.activity.TRACK

class PlayerActivity : AppCompatActivity() {

    private lateinit var track: Track
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var viewModel: PlayerActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[PlayerActivityViewModel::class.java]


        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getPreparePlayer().observe(this){finish ->
            binding.playButton.isEnabled = finish
            binding.playButton.setImageResource(R.drawable.play);
        }
        viewModel.getPlayerButtonIsPlay().observe(this){ isPlay ->
            if(isPlay) binding.playButton.setImageResource(R.drawable.play)
            else binding.playButton.setImageResource(R.drawable.pause)
        }

        viewModel.getCurrentTrackTimePosition().observe(this){ currentTrackTimePosition ->
            binding.currentTime.text = currentTrackTimePosition
        }


        track = intent.getParcelableExtra(TRACK) ?: Track("","","","",
            "","","","","")

        binding.buttonBack.setOnClickListener {
            finish()
        }

        downloadData()

        viewModel.preparePlayer(track)
        viewModel.setTrackTimeRunnable()

        binding.playButton.setOnClickListener {
            viewModel.playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        binding.playButton.setImageResource(R.drawable.play);
        viewModel.pausePlayer()    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.releasePlayer()
    }

    private fun downloadData(){
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