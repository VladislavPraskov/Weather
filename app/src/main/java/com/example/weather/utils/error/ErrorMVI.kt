package com.example.weather.utils.error

import android.content.Context
import com.example.weather.utils.network.ApiResult

data class ErrorMVI(val code: Int? = null, val message: String? = null) {

    companion object {
        fun create(context: Context, error: ApiResult.NetworkError?): ErrorMVI {
            return ErrorMVI(code = error?.code,
                message = error?.getError(context))
        }
    }
}