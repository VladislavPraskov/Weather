package com.example.weather.presenter.main.mvi

import com.example.weather.models.WeatherUI
import com.example.weather.utils.network.ApiResult

sealed class MainResultAction {
    data class Loading(val isLoading: Boolean = true) : MainResultAction()
    object SuccessEmpty : MainResultAction()
    object Nothing : MainResultAction()
    data class Error(val networkError: ApiResult.NetworkError?) : MainResultAction()
    data class Success(val data: WeatherUI?) : MainResultAction()
    object UpdateTime : MainResultAction()

    companion object {
        fun getSuccessOrEmpty(weather: WeatherUI?): MainResultAction {
            return if (weather == null) SuccessEmpty
            else Success(weather)
        }
    }
}
