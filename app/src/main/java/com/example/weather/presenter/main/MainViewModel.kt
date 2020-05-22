package com.example.weather.presenter.main

import android.app.Application
import androidx.lifecycle.liveData
import com.example.weather.domain.main.MainInteractor
import com.example.weather.presenter.main.mvi.MainAction
import com.example.weather.presenter.main.mvi.MainResultAction
import com.example.weather.presenter.main.mvi.MainViewState
import com.example.weather.utils.mvi.BaseViewModel


class MainViewModel(app: Application, private val interactor: MainInteractor) :
    BaseViewModel<MainAction, MainViewState, MainResultAction>(app,
        MainViewState()
    ) {

    override fun handleNewAction(action: MainAction) = liveData<MainResultAction> {
        when (action) {
            is MainAction.GetCityWeather -> {
                emitSource(interactor.getCityByName(action.cityName))
            }
            is MainAction.LoadCurrentCity -> {
                emitSource(interactor.getCurrentCity())
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
