package com.example.weather.presenter.main.mvi

import com.example.weather.models.WeatherUI
import com.example.weather.utils.network.ApiResult

sealed class MainResultAction {
    data class Loading(val isLoading: Boolean = true) : MainResultAction()
    data class SuccessEmpty(val isLoading: Boolean = false) : MainResultAction()
    object Nothing : MainResultAction()
    data class Error(val networkError: ApiResult.NetworkError?) : MainResultAction()
    data class Success(val data: WeatherUI?, val isLoading: Boolean = false) : MainResultAction()
    object UpdateTime : MainResultAction()

    companion object {
        fun getSuccessOrEmpty(weather: WeatherUI?, first: Boolean): MainResultAction {
            return if (weather == null) SuccessEmpty(isLoading = first)
            else Success(weather, isLoading = first)
        }
    }
}
