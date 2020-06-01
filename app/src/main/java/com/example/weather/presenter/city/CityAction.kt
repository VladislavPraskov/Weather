package com.example.weather.presenter.city

sealed class CityAction {
    data class LoadByQuery(val q: String) : CityAction()
    object LoadCityFromDB : CityAction()
}
