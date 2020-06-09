package com.example.weather.utils.mapper

import com.example.weather.data.db.city.CityEntity
import com.example.weather.models.city.CityResponse
import com.example.weather.models.city.CityUI
import kotlin.math.absoluteValue

fun mapToCityUI(city: CityResponse?): List<CityUI>? {
    return if (city?.results.isNullOrEmpty()) null
    else city?.results?.mapNotNull {
        it?.let { c ->
            if (c.components?.type in listOf("city", "village", "state")) {
                CityUI(
                    cityName = c.components?.city ?: "",
                    country = c.components?.country ?: "",
                    idString = getId(c),
                    offsetSec = c.annotations?.timezone?.offsetSec ?: 0,
                    utc = getUtc(c),
                    lg = c.geometry?.lng ?: 0.0,
                    lt = c.geometry?.lat ?: 0.0,
                    isCurrentSelected = false
                )
            } else null
        }
    }?.filter { it.cityName.isNotEmpty() }?.distinctBy { it.idString }?.take(10)
}

private fun getId(c: CityResponse.Result): String {
    val code = c.components?.countryCode ?: ""
    val lngLt = "%.4f".format(c.geometry?.lng) + "%.4f".format(c.geometry?.lat)
    return code + lngLt
}

private fun getUtc(c: CityResponse.Result): String {
    val utcOffset = (c.annotations?.timezone?.offsetSec ?: 0) / 3600
    return if (utcOffset < 0) "UTC -${utcOffset.absoluteValue}:00" else "UTC +${utcOffset.absoluteValue}:00"
}

fun mapToCityEntity(city: CityUI): CityEntity {
    return CityEntity(
        lat = city.lt,
        lon = city.lg,
        idString = city.idString,
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
        idString = entity.idString,
        cityName = entity.cityName,
        utc = entity.utc,
        isCache = true,
        country = entity.country,
        offsetSec = entity.offsetSec,
        isCurrentSelected = entity.isCurrentSelected
    )
}