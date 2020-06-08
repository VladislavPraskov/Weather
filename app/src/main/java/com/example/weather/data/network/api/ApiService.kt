package com.example.weather.data.network.api

import com.example.weather.models.main.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("onecall")
    suspend fun getWeatherForecast(
        @Query("lat") lan: Double?,
        @Query("lon") lot: Double?,
        @Query("exclude ") type: String? = "hourly,daily",
        @Query("units") unit: String? = "metric"
    ): WeatherResponse
}