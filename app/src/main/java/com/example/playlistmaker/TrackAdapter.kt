package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(
    private var tracks: List<Track>) : RecyclerView.Adapter<TrackViewHolder> () {

    var onItemClick : ((Track) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(track)
        }
    }

    fun updateData(newTracks: List<Track>) {
        tracks = newTracks
        notifyDataSetChanged()
    }

    override fun getItemCount() = tracks.size
}