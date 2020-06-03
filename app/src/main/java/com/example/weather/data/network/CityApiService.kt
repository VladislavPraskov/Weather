package com.example.weather.data.network

import com.example.weather.models.CityResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CityApiService {

    @GET("json")
    suspend fun getCityByQuery(
        @Query("q") name: String?,
        @Query("language") lang: String? = "en",
//        @Query("searchlang") lang: String? = Locale.getDefault().language,
        @Query("limit") limit: Int? = 30
    ): CityResponse
}