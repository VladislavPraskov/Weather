package com.example.weather.utils

import com.example.weather.data.db.city.CityEntity
import com.example.weather.models.CityResponse
import com.example.weather.models.CityUI
import kotlin.math.absoluteValue

fun mapToCityUI(city: CityResponse?): List<CityUI>? {
    return if (city?.results.isNullOrEmpty()) null
    else city?.results?.mapNotNull {
        it?.let { c ->
            if (c.components?.type in listOf("city", "village", "state")) {
                val utcOffset = (c.annotations?.timezone?.offsetSec ?: 0) / 3600
                val utc =
                    if (utcOffset < 0) "UTC -${utcOffset.absoluteValue}:00" else "UTC +${utcOffset.absoluteValue}:00"
                CityUI(
                    cityName = c.components?.city ?: "",
                    country = c.components?.country ?: "",
                    countryAndPostCode = (c.components?.countryCode ?: "") + (c.components?.postcode
                        ?: ""),
                    offsetSec = c.annotations?.timezone?.offsetSec ?: 0,
                    utc = utc,
                    lg = c.geometry?.lng ?: 0.0,
                    lt = c.geometry?.lat ?: 0.0,
                    isCurrentSelected = false
                )
            } else null
        }
    }?.filter { it.cityName.isNotEmpty() }?.take(10)
}

fun mapToCityEntity(city: CityUI): CityEntity {
    return CityEntity(
        lat = city.lt,
        lon = city.lg,
        countryAndPostCode = city.countryAndPostCode,
        cityName = city.cityName,
        utc = city.utc,
        country = city.country,
        offsetSec = city.offsetSec,
        isCurrentSelected = true
    )
}

fun mapToCityUI(entity: CityEntity): CityUI {
    return CityUI(
        lt = entity.lat,
        lg = entity.lon,
        countryAndPostCode = entity.countryAndPostCode,
        cityName = entity.cityName,
        utc = entity.utc,
        country = entity.country,
        offsetSec = entity.offsetSec,
        isCurrentSelected = entity.isCurrentSelected
    )
}