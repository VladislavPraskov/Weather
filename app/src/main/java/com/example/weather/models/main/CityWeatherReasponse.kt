package com.example.weather.models.main


import com.google.gson.annotations.SerializedName

data class CityWeatherReasponse(
    val coord: Coord? = null,
    val weather: List<Weather?>? = null,
    val base: String? = null,
    val main: Main? = null,
    val visibility: Int? = null,
    val wind: Wind? = null,
    val clouds: Clouds? = null,
    val dt: Int? = null,
    val sys: Sys? = null,
    val timezone: Int? = null,
    val id: Int? = null,
    val name: String? = null,
    val cod: Int? = null
) {
    data class Coord(
        val lon: Double? = null,
        val lat: Double? = null
    )

    data class Weather(
        val id: Int? = null,
        val main: String? = null,
        val description: String? = null,
        val icon: String? = null
    )

    data class Main(
        val temp: Double? = null,
        @SerializedName("feels_like")
        val feelsLike: Double? = null,
        @SerializedName("temp_min")
        val tempMin: Double? = null,
        @SerializedName("temp_max")
        val tempMax: Double? = null,
        val pressure: Int? = null,
        val humidity: Int? = null
    )

    data class Wind(
        val speed: Double? = null,
        val deg: Int? = null
    )

    data class Clouds(
        val all: Int? = null
    )

    data class Sys(
        val type: Int? = null,
        val id: Int? = null,
        val country: String? = null,
        val sunrise: Int? = null,
        val sunset: Int? = null
    )
}