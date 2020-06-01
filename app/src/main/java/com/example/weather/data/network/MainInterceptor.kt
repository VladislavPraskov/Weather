package com.example.weather.data.network

import okhttp3.Interceptor
import okhttp3.Response

class MainInterceptor(val name: String, private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url = request.url()
            .newBuilder()
            .addQueryParameter(name, apiKey)
            .build()
        request = request.newBuilder().url(url).build()
        return chain.proceed(request)
    }
}