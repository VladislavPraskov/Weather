package com.example.weather.models.main


import com.google.gson.annotations.SerializedName

data class WeatherForecast(
    val cod: String? = null,
    val message: Double? = null,
    val cnt: Long? = null,
    val list: List<WeatherMain>? = null,
    val city: City? = null
) {
    data class WeatherMain(
        val dt: Long? = null,
        val main: Main? = null,
        val weather: List<Weather?>? = null,
        val clouds: Clouds? = null,
        val wind: Wind? = null,
        val sys: Sys? = null,
        @SerializedName("dt_txt")
        val dtTxt: String? = null
    ) {
        data class Main(
            val temp: Double? = null,
            @SerializedName("feels_like")
            val feelsLike: Double? = null,
            @SerializedName("temp_min")
            val tempMin: Double? = null,
            @SerializedName("temp_max")
            val tempMax: Double? = null,
            val pressure: Long? = null,
            @SerializedName("sea_level")
            val seaLevel: Long? = null,
            @SerializedName("grnd_level")
            val grndLevel: Long? = null,
            val humidity: Long? = null,
            @SerializedName("temp_kf")
            val tempKf: Double? = null
        )

        data class Weather(
            val id: Long? = null,
            val main: String? = null,
            val description: String? = null,
            val icon: String? = null
        )

        data class Clouds(
            val all: Long? = null
        )

        data class Wind(
            val speed: Double? = null,
            val deg: Double? = null
        )

        data class Sys(
            val pod: String? = null
        )
    }

    data class City(
        val id: Long? = null,
        val name: String? = null,
        val coord: Coord? = null,
        val country: String? = null,
        val population: Long? = null,
        val timezone: Long? = null,
        val sunrise: Long? = null,
        val sunset: Long? = null
    ) {
        data class Coord(
            val lat: Double? = null,
            val lon: Double? = null
        )
    }
}