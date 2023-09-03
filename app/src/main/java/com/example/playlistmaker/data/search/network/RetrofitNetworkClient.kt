package com.example.playlistmaker.data.search.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.playlistmaker.data.search.dto.Response
import com.example.playlistmaker.data.search.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.util.concurrent.TimeoutException

class RetrofitNetworkClient(private val context: Context) : NetworkClient {

    private val itunesUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunes = retrofit.create(ITunesApiService::class.java)

    override fun doRequest(dto: Any): Response {
        if (isConnected() == false) {
            return Response().apply { resultCode = -1 }
        }
        if (dto !is TrackSearchRequest) {
            return Response().apply { resultCode = 400 }
        }

        try{
            val response = itunes.search(dto.expression).execute()
            val body = response.body()
            return if (body != null) {
                body.apply { resultCode = response.code() }
            } else {
                Response().apply { resultCode = response.code() }
            }
        }catch (e : Exception){
           return Response().apply { resultCode = -2 }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}
