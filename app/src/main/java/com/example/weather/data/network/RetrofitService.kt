package com.example.weather.data.network

import com.example.weather.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitService {
    const val BASE_WEATHER_URL = "https://api.openweathermap.org/data/2.5/"
    const val BASE_CITY_URL = "https://api.opencagedata.com/geocode/v1/"
}

private fun getLoggingInterceptor() = HttpLoggingInterceptor().apply {
    level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
    else HttpLoggingInterceptor.Level.NONE
}


fun getClient(interceptor: Interceptor): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(getLoggingInterceptor())
        .addInterceptor(interceptor)
        .readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
}

fun getRetrofitBuilder(url: String): Retrofit.Builder {
    return Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create())
}

fun getWeatherApi(retrofit: Retrofit.Builder, client: OkHttpClient): ApiService {
    return retrofit.client(client).build().create(ApiService::class.java)
}

fun getCityApi(retrofit: Retrofit.Builder, client: OkHttpClient): CityApiService {
    return retrofit.client(client).build().create(CityApiService::class.java)
}