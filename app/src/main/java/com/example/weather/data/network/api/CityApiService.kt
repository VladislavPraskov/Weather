package com.example.weather.data.network.api

import com.example.weather.models.CityResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CityApiService {

    @GET("json")
    suspend fun getCityByQuery(
        @Query("q") name: String?,
        @Query("language") lang: String? = "en",
        @Query("limit") limit: Int? = 30
    ): CityResponse
}