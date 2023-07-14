package com.example.playlistmaker.domain.api

import com.example.playlistmaker.data.search.TrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TrackApiService {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<TrackResponse>
}