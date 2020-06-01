package com.example.weather.presenter.city

sealed class CityResultAction {
    object Loading : CityResultAction()
    data class SomeAction(val p0: String? = "") : CityResultAction()
}
