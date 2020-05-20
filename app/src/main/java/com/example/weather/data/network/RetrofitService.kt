package com.example.weather.data.network

import com.example.weather.BuildConfig
import com.example.weather.data.network.RetrofitService.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitService {
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
}

private fun getLoggingInterceptor() = HttpLoggingInterceptor().apply {
    level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
    else HttpLoggingInterceptor.Level.NONE
}


fun getClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(getLoggingInterceptor())
        .addInterceptor(MainInterceptor())
        .readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
}

fun getRetrofitBuilder(client: OkHttpClient): Retrofit.Builder {
    return Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
}

fun getApi(retrofit: Retrofit.Builder, client: OkHttpClient): ApiService {
    return retrofit.client(client).build().create(ApiService::class.java)
}