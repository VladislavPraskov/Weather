package com.example.weather.presenter.main

import android.app.Application
import androidx.lifecycle.liveData
import com.devpraskov.android_ext.currentTimeSec
import com.devpraskov.android_ext.dayAndTime
import com.example.weather.domain.main.MainInteractor
import com.example.weather.presenter.main.mvi.MainAction
import com.example.weather.presenter.main.mvi.MainResultAction
import com.example.weather.presenter.main.mvi.MainViewState
import com.example.weather.utils.error.ErrorMVI
import com.example.weather.utils.models.toEvent
import com.example.weather.utils.mvi.BaseViewModel


class MainViewModel(app: Application, private val interactor: MainInteractor) :
    BaseViewModel<MainAction, MainViewState, MainResultAction>(
        app,
        MainViewState()
    ) {

    override fun handleNewAction(action: MainAction) = liveData<MainResultAction> {
        when (action) {
            is MainAction.GetCityWeather -> {
                emitSource(interactor.getCityByName(action.cityName))
            }
            is MainAction.LoadCurrentCity -> {
                emit(MainResultAction.Loading())
                emitSource(interactor.getCurrentCity())
            }
            is MainAction.UpdateTime -> {
                emit(MainResultAction.UpdateTime)
            }
        }
    }

    override fun reduceNewViewState(
        currentViewState: MainViewState,
        result: MainResultAction
    ): MainViewState {
        return when (result) {
            is MainResultAction.Loading -> currentViewState.copy(isLoading = true)
            is MainResultAction.SuccessEmpty -> currentViewState.copy(isLoading = result.isLoading)
            is MainResultAction.Nothing -> currentViewState
            is MainResultAction.Error -> currentViewState.copy(
                isLoading = false,
                error = ErrorMVI.create(context, error = result.networkError)
            )
            is MainResultAction.Success -> currentViewState.copy(
                isLoading = result.isLoading,
                data = result.data.toEvent(),
                time = dayAndTime()
            )
            is MainResultAction.UpdateTime -> currentViewState.copy(
                isLoading = false,
                time = dayAndTime()
            )

        }
    }
}
