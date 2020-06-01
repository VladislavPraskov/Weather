package com.example.weather.utils

import com.example.weather.models.CityResponseModel
import com.example.weather.models.CityUI

fun mapToCityUI(city: CityResponseModel?): List<CityUI>? {
    return city?.geonames?.mapNotNull {
        if (it?.geonameId == null || it.name.isNullOrEmpty())
            null
        else
            CityUI(
                cityName = it.name,
                geonameId = it.geonameId,
                lg = it.lng?.toDoubleOrNull() ?: 0.0,
                lt = it.lat?.toDoubleOrNull() ?: 0.0
            )
    }
}