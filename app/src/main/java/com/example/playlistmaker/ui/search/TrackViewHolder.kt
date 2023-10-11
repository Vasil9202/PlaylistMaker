package com.example.playlistmaker.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(parent: ViewGroup,
                      private val clickListener: ItemClickListener,
) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)) {



       var artworkUrl100View: ImageView = itemView.findViewById(R.id.artworkUrl100)
       var trackNameView: TextView = itemView.findViewById(R.id.trackName)
       var artistNameView: TextView = itemView.findViewById(R.id.artistName)
       var trackTimeView: TextView = itemView.findViewById(R.id.trackTime)



    fun bind(model: Track) {
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .centerCrop()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.dp_2)))
            .placeholder(R.drawable.placeholder)
            .into(artworkUrl100View)
        trackNameView.text = model.trackName
        artistNameView.text = model.artistName
        trackTimeView.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMilliSec.toLong())
        itemView.setOnClickListener { clickListener.onTrackClick(model) }

    }
}

interface ItemClickListener {
    fun onTrackClick(track: Track)

}