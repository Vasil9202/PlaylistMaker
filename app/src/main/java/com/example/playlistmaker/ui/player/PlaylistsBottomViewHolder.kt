package com.example.playlistmaker.ui.player

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.Playlist

class PlaylistsViewHolder(parent: ViewGroup,
                          private val clickListener: PlaylistBottomItemClickListener,
) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.playlist_view_bottom_sheet, parent, false)) {

    private val placeholderPath: ImageView = itemView.findViewById(R.id.playlists_label)
    private val name: TextView = itemView.findViewById(R.id.playlists_name)
    private val tracksCount: TextView = itemView.findViewById(R.id.playlists_track_count)

    fun bind(playlist: Playlist) {
        if(playlist.placeholderPath.isNotEmpty()){
            placeholderPath.setImageURI(Uri.parse(playlist.placeholderPath))
        }
        name.text = playlist.name
        tracksCount.text = "${playlist.tracksCount} треков"
        itemView.setOnClickListener { clickListener.onPlaylistClick(playlist) }

    }
}

interface PlaylistBottomItemClickListener {
    fun onPlaylistClick(playlist: Playlist)

}