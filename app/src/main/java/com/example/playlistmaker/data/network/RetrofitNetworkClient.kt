package com.example.playlistmaker.data.network

import android.content.Context
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val context: Context) : NetworkClient {

    private val itunesUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunes = retrofit.create(TrackApiService::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is TrackSearchRequest) {
            val resp = itunes.search(dto.expression).execute()

            val body = resp.body() ?: Response()

            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}