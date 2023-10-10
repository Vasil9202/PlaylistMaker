package com.example.playlistmaker.ui.media_lib

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.model.Track

class TrackBottomAdapter (private val clickListener: TrackBottomItemClickListener
) : RecyclerView.Adapter<TracksBottomViewHolder> () {

    var tracks = ArrayList<Track>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksBottomViewHolder {
        return TracksBottomViewHolder(parent,clickListener)
    }

    override fun onBindViewHolder(holder: TracksBottomViewHolder, position: Int) {
        holder.bind(tracks.get(position))
    }

    override fun getItemCount() = tracks.size

}