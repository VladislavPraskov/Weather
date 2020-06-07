package com.example.weather.utils.network

import com.example.weather.BuildConfig
import com.example.weather.utils.network.ApiResult.NetworkError
import com.example.weather.R
import retrofit2.HttpException
import java.io.IOException
import java.lang.IllegalStateException


/**
 * Reference: https://medium.com/@douglas.iacovelli/how-to-handle-errors-with-retrofit-and-coroutines-33e7492a912
 */

const val NETWORK_ERROR_CODE = 503

suspend fun <T> safeApiCall(
    apiCall: suspend () -> T?
): ApiResult<T?> {
    return try {
        ApiResult.Success(apiCall.invoke())
    } catch (throwable: Throwable) {
        throwable.printStackTrace() //todo crashlytics
        when (throwable) {
            is IOException -> {
                NetworkError(
                    code = NETWORK_ERROR_CODE,
                    errorRes = R.string.error_connection
                )
            }
            is HttpException -> {
                val code = throwable.code()
                var errorResponse = convertErrorBody(throwable)
                val resId = if (throwable.code() in 500..599) {
                    errorResponse = null
                    R.string.backend_connection_error
                } else R.string.unknown_error

                NetworkError(code = code, errorStr = errorResponse, errorRes = resId)
            }
            else -> { //todo добавить возможность кастомных ошибок и извлечения текста из них
                NetworkError(code = null, errorRes = R.string.unknown_error)
            }
        }
    }

}


private fun convertErrorBody(throwable: HttpException): String? {
    return try {
        throwable.response()?.errorBody()?.string()
    } catch (exception: Exception) {
        null
    }
}


suspend fun <T> safeCacheCall(
    cacheCall: (suspend () -> T?)?,
    onSuccess: (suspend (T?) -> Unit?)? = null,
    onError: (suspend () -> Unit?)? = null
): T? {
    return try {
        val result = cacheCall?.invoke()
        onSuccess?.invoke(result)
        result
    } catch (t: Throwable) {
        if (BuildConfig.DEBUG)
            throw t
        else {
            onError?.invoke() //todo crashlytics
            null
        }
    }
}

