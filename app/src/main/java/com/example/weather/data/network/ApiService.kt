package com.example.weather.data.network

import com.example.weather.models.main.HourlyWeather
import com.example.weather.models.main.WeatherForecast
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("weather")
    suspend fun getWeatherCity(@Query("q") city: String): WeatherForecast

    @GET("onecall")
    suspend fun getWeatherCityByLocationHourlyForecast(
        @Query("lat") lan: Double?,
        @Query("lon") lot: Double?,
        @Query("exclude ") type: String? = "hourly,daily",
        @Query("units") unit: String? = "metric"
    ): HourlyWeather
//
//    @GET("forecast/hourly")
//    suspend fun getWeatherCityById(@Query("q") city: String = "Таганрог"): HourlyWeatherForecast
}