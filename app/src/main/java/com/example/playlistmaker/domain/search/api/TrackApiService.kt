package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.data.search.dto.TrackSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TrackApiService {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<TrackSearchResponse>
}