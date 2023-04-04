package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val artworkUrl100View: ImageView
    private val trackNameView: TextView
    private val artistNameView: TextView
    private val trackTimeView: TextView

    init {
        artworkUrl100View = itemView.findViewById(R.id.artworkUrl100)
        trackNameView = itemView.findViewById(R.id.trackName)
        artistNameView = itemView.findViewById(R.id.artistName)
        trackTimeView = itemView.findViewById(R.id.trackTime)
    }

    fun bind(model: Track) {
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .centerCrop()
            .transform(RoundedCorners(
                itemView.resources.getDimensionPixelSize(R.dimen.DP2)))
            .placeholder(R.drawable.placeholder)
            .into(artworkUrl100View)
        trackNameView.text = model.trackName
        artistNameView.text = model.artistName
        trackTimeView.text = model.trackTime
    }
}