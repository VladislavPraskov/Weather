package com.example.weather.presenter.main.mvi

sealed class MainResultAction {
    object Loading : MainResultAction()
    data class SomeAction(val p0: String? = "") : MainResultAction()
}
