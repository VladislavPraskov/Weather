package com.example.weather.data.error

import com.example.weather.R
import com.example.weather.utils.network.CustomException

class LocationSecurityException : CustomException() {
    companion object {
        const val LOCATION_SECURITY_CODE = 2341
    }

    override fun getCode(): Int? = LOCATION_SECURITY_CODE

    override fun getErrorRes(): Int? {
        return R.string.location_permission
    }

}