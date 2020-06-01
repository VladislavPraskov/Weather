package com.example.weather.data.network


import androidx.lifecycle.liveData
import com.example.weather.utils.network.ApiResult
import com.example.weather.utils.network.NETWORK_ERROR_CODE
import com.example.weather.utils.network.safeApiCall
import kotlinx.coroutines.Dispatchers

abstract class NetworkResourse<NetworkObj, ResultAction>(
    private val apiCall: suspend () -> NetworkObj,
    private val onSuccess: (ApiResult.Success<NetworkObj?>) -> ResultAction,
    private val onError: suspend (ApiResult.NetworkError) -> ResultAction
) {
    val result = liveData(Dispatchers.IO) {
        val apiResult = safeApiCall(apiCall)
        when (apiResult) {
            is ApiResult.Success -> emit(onSuccess.invoke(apiResult))
            is ApiResult.NetworkError -> {
                if (apiResult.code == NETWORK_ERROR_CODE) return@liveData
                else emit(onError.invoke(apiResult))
            }
        }
    }

    companion object {
        fun <NetworkObj, ResultAction> create(
            apiCall: suspend () -> NetworkObj,
            onSuccess: (ApiResult.Success<NetworkObj?>) -> ResultAction,
            onError: suspend (ApiResult.NetworkError) -> ResultAction
        ): NetworkResourse<NetworkObj, ResultAction> {
            return object :
                NetworkResourse<NetworkObj, ResultAction>(apiCall, onSuccess, onError) {}
        }
    }
}