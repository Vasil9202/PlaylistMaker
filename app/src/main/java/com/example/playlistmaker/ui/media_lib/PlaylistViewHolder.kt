package com.example.playlistmaker.ui.media_lib

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.Playlist


class PlaylistViewHolder(parent: ViewGroup,
                         private val clickListener: PlaylistItemClickListener,
) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.playlist_view, parent, false)) {

    private val placeholderPath: ImageView = itemView.findViewById(R.id.placeholder_path)
    private val name: TextView = itemView.findViewById(R.id.playlist_name)
    private val tracksCount: TextView = itemView.findViewById(R.id.tracks_count)

    fun bind(playlist: Playlist) {
        if(playlist.placeholderPath.isNotEmpty()){
            placeholderPath.setImageURI(Uri.parse(playlist.placeholderPath))
        }
        name.text = playlist.name
        tracksCount.text = "${playlist.tracksCount} треков"
        itemView.setOnClickListener { clickListener.onPlaylistClick(playlist) }

    }
}

interface PlaylistItemClickListener {
    fun onPlaylistClick(playlist: Playlist)

}

