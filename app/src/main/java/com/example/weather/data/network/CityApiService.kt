package com.example.weather.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface CityApiService {

    @GET("searchJSON")
    fun getCityByQuery(
        @Query("name") name: String?,
        @Query("style") style: String? = "LONG"
    )
}