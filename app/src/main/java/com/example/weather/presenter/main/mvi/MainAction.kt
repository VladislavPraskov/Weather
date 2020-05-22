package com.example.weather.presenter.main.mvi

sealed class MainAction {
    data class GetCityWeather(val cityName: String = "") : MainAction()
    object LoadCurrentCity : MainAction()
}
