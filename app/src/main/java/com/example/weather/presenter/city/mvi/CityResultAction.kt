package com.example.weather.presenter.city.mvi

import com.example.weather.models.city.CityUI
import com.example.weather.utils.network.ApiResult

sealed class CityResultAction {
    object Loading : CityResultAction()
    object SuccessEmpty : CityResultAction()
    object Nothing : CityResultAction()
    data class Error(val networkError: ApiResult.NetworkError?) : CityResultAction()
    data class Success(val data: List<CityUI>?, val isCache: Boolean
    ) : CityResultAction()
    data class CitySaved(val cachedCity: List<CityUI>?) : CityResultAction()

    companion object {
        fun getSuccessOrEmpty(cities: List<CityUI>?, isCache: Boolean): CityResultAction {
            return if (cities == null) SuccessEmpty
            else Success(
                cities,
                isCache
            )
        }
    }
}