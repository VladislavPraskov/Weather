package com.example.weather.presenter.main

sealed class MainResultAction {
    object Loading : MainResultAction()
    data class SomeAction(val p0: String? = "") : MainResultAction()
}
