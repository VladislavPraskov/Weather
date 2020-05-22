package com.example.weather.presenter.second

sealed class DetailsResultAction {
    object Loading : DetailsResultAction()
    data class SomeAction(val p0: String? = "") : DetailsResultAction()
}
