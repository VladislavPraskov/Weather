package com.example.weather.presenter.city.mvi

import com.example.weather.models.city.CityUI

sealed class CityAction {
    data class LoadByQuery(val q: String) : CityAction()
    data class SaveCity(val city: CityUI) : CityAction()
    data class DeleteCity(val deletedCity: CityUI) : CityAction()
    object LoadCityFromDB : CityAction()
}
