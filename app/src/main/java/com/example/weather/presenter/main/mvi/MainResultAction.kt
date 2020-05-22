package com.example.weather.presenter.main.mvi

import com.example.weather.data.db.weather.WeatherEntity
import com.example.weather.models.main.WeatherUiModels
import com.example.weather.utils.network.ApiResult

sealed class MainResultAction {
    data class Loading(val isLoading: Boolean = true) : MainResultAction()
    object SuccessEmptyList : MainResultAction()
    object Nothing : MainResultAction()
    data class Error(val networkError: ApiResult.NetworkError?) : MainResultAction()
    data class Success(val data: WeatherUiModels) : MainResultAction()

    companion object {
        fun getSuccessOrEmpty(resultObj: List<WeatherEntity>?): MainResultAction {
            return if (resultObj.isNullOrEmpty()) SuccessEmptyList
            else Success(  WeatherUiModels.create(resultObj) )
        }
    }
}
