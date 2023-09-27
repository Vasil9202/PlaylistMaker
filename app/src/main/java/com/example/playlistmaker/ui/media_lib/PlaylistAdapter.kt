package com.example.playlistmaker.ui.media_lib

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.model.Playlist


class PlaylistAdapter(private val clickListener: PlaylistItemClickListener
) : RecyclerView.Adapter<PlaylistViewHolder> () {

    var playlists = ArrayList<Playlist>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return PlaylistViewHolder(parent,clickListener)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists.get(position))
    }

    override fun getItemCount() = playlists.size
}