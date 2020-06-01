package com.example.weather.models


data class CityResponseModel( //todo refactoring
    val totalResultsCount: Int? = null,
    val geonames: List<Geoname?>? = null
) {
    data class Geoname(
        val lng: String? = null,
        val geonameId: Long? = null,
        val countryCode: String? = null,
        val name: String? = null,
        val toponymName: String? = null,
        val lat: String? = null,
        val fcl: String? = null,
        val fcode: String? = null
    )
}