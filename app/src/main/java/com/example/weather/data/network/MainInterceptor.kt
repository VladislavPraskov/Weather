package com.example.weather.data.network

import okhttp3.Interceptor
import okhttp3.Response

class MainInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url = request.url()
            .newBuilder()
            .addQueryParameter("appid", WeatherKeyGitIgnore.AUTH_KEY)
            .build()
        request = request.newBuilder().url(url).build()
        return chain.proceed(request)
    }
}