package com.example.weather.data.network

import com.example.weather.models.CityResponseModel
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

interface CityApiService {

    @GET("searchJSON")
    suspend fun getCityByQuery(
        @Query("name") name: String?,
        @Query("style") style: String? = "SHORT",
        @Query("searchlang") lang: String? = Locale.getDefault().language
    ): CityResponseModel
}