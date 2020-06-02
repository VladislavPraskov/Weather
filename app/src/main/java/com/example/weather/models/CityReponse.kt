package com.example.weather.models


import com.google.gson.annotations.SerializedName

data class CityResponse(
    val documentation: String? = null,
    val results: List<Result?>? = null,
    val status: Status? = null,
    val thanks: String? = null,
    @SerializedName("total_results")
    val totalResults: Int? = null
) {

    data class Result(
        val annotations: Annotations? = null,
        val components: Components? = null,
        val formatted: String? = null,
        val geometry: Geometry? = null
    ) {
        data class Components(
            @SerializedName("_type")
            val type: String? = null,
            val city: String? = null,
            val state: String? = null,
            val country: String? = null,
            @SerializedName("country_code")
            val countryCode: String? = null,
            val continent: String? = null,
            val county: String? = null,
            val postcode: String? = null
        )
        data class Geometry(
            val lat: Double? = null,
            val lng: Double? = null
        )
        data class Annotations(
            val timezone: Timezone? = null
        ) {
            data class Timezone(
                @SerializedName("offset_sec")
                val offsetSec: Long? = null,
                @SerializedName("offset_string")
                val offsetString: String? = null,
                @SerializedName("short_name")
                val shortName: String? = null
            )
        }
    }
    data class Status(
        val code: Int? = null,
        val message: String? = null
    )
}