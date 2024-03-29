package com.example.playlistmaker.ui.player

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.model.Playlist

class PlaylistBottomAdapter (private val clickListener: PlaylistBottomItemClickListener
) : RecyclerView.Adapter<PlaylistsViewHolder> () {

    var playlists = ArrayList<Playlist>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        return PlaylistsViewHolder(parent,clickListener)
    }

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        holder.bind(playlists.get(position))
    }

    override fun getItemCount() = playlists.size

}