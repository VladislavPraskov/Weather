package com.example.weather.models.main


import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val lat: Double? = null,
    val lon: Double? = null,
    val timezone: String? = null,
    @SerializedName("timezone_offset")
    val timezoneOffset: Long? = null,
    val current: Current? = null,
    val hourly: List<Hourly?>? = null,
    val daily: List<Daily?>? = null
) {
    data class Current(
        val dt: Long? = null,
        val sunrise: Long? = null,
        val sunset: Long? = null,
        val temp: Double? = null,
        @SerializedName("feels_like")
        val feelsLike: Double? = null,
        val pressure: Long? = null,
        val humidity: Long? = null,
        @SerializedName("dew_point")
        val dewPoint: Double? = null,
        val visibility: Long? = null,
        @SerializedName("wind_speed")
        val windSpeed: Double? = null,
        val weather: List<Weather?>? = null
    )

    data class Hourly(
        val dt: Long? = null,
        val temp: Double? = null,
        @SerializedName("feels_like")
        val feelsLike: Double? = null,
        val pressure: Long? = null,
        val humidity: Long? = null,
        @SerializedName("dew_poLong")
        val dewPoLong: Double? = null,
        @SerializedName("wind_speed")
        val windSpeed: Double? = null,
        val weather: List<Weather?>? = null
    )

    data class Daily(
        val dt: Long? = null,
        val sunrise: Long? = null,
        val sunset: Long? = null,
        val temp: Temp? = null,
        @SerializedName("feels_like")
        val feelsLike: FeelsLike? = null,
        val pressure: Long? = null,
        val humidity: Long? = null,
        @SerializedName("dew_poLong")
        val dewPoLong: Double? = null,
        @SerializedName("wind_speed")
        val windSpeed: Double? = null,
        @SerializedName("wind_deg")
        val windDeg: Long? = null,
        val weather: List<Weather?>? = null,
        val clouds: Long? = null,
        val uvi: Double? = null,
        val rain: Double? = null
    ) {
        data class Temp(
            val day: Double? = null,
            val min: Double? = null,
            val max: Double? = null,
            val night: Double? = null,
            val eve: Double? = null,
            val morn: Double? = null
        )

        data class FeelsLike(
            val day: Double? = null,
            val night: Double? = null,
            val eve: Double? = null,
            val morn: Double? = null
        )


    }
}
data class Weather(
    val id: Int? = null,
    val main: String? = null,
    val description: String? = null,
    val icon: String? = null
)