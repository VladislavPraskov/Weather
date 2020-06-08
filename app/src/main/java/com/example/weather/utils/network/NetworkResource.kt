package com.example.weather.utils.network


import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

abstract class NetworkResource<NetworkObj, ResultAction>(
    private val apiCall: suspend () -> NetworkObj,
    private val onSuccess: suspend (ApiResult.Success<NetworkObj?>) -> ResultAction,
    private val onError: suspend (ApiResult.NetworkError) -> ResultAction
) {
    val result = flow {
        val apiResult = safeApiCall(apiCall)
        delay(1)
        when (apiResult) {
            is ApiResult.Success -> emit(onSuccess.invoke(apiResult))
            is ApiResult.NetworkError -> emit(onError.invoke(apiResult))
        }
    }

    companion object {
        fun <NetworkObj, ResultAction> create(
            apiCall: suspend () -> NetworkObj,
            onSuccess: suspend (ApiResult.Success<NetworkObj?>) -> ResultAction,
            onError: suspend (ApiResult.NetworkError) -> ResultAction
        ): NetworkResource<NetworkObj, ResultAction> {
            return object :
                NetworkResource<NetworkObj, ResultAction>(apiCall, onSuccess, onError) {}
        }
    }
}