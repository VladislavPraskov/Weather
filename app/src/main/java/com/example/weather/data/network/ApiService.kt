package com.example.weather.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("weather")
    suspend fun getWeatherCity(@Query("q") city: String = "Moscow"): Any
}