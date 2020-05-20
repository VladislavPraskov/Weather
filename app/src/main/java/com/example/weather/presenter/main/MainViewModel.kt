package com.example.weather.presenter.main

import android.app.Application
import androidx.lifecycle.liveData
import com.example.weather.domain.main.MainInteractor
import com.example.weather.utils.mvi.BaseViewModel


class MainViewModel(app: Application, private val interactor: MainInteractor) :
    BaseViewModel<MainAction, MainViewState, MainResultAction>(app, MainViewState()) {

    override fun handleNewAction(action: MainAction) = liveData {
        when (action) {
            is MainAction.SomeAction -> {
                emitSource(interactor.getWeatherCity())
            }
            is MainAction.SomeAction2 -> {
                emit(MainResultAction.SomeAction())
            }
        }
    }

    override fun reduceNewViewState(
        currentViewState: MainViewState,
        result: MainResultAction
    ): MainViewState {
        return when (result) {
            is MainResultAction.SomeAction -> currentViewState.copy(isLoading = true)
            is MainResultAction.Loading -> currentViewState.copy(isLoading = true)
        }
    }
}
