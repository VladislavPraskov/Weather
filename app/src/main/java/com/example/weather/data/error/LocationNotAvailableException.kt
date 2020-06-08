package com.example.weather.data.error

import com.example.weather.R
import com.example.weather.utils.network.CustomException

class LocationNotAvailableException : CustomException() {
    companion object {
        const val LOCATION_NOT_AVAILABLE_CODE = 2323
    }

    override fun getCode(): Int? = LOCATION_NOT_AVAILABLE_CODE

    override fun getErrorRes(): Int? {
        return R.string.location_not_available_error
    }

}