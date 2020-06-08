package com.example.weather.presenter.main

import android.app.Application
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.devpraskov.android_ext.dayAndTime
import com.example.weather.domain.main.MainInteractor
import com.example.weather.presenter.main.mvi.MainAction
import com.example.weather.presenter.main.mvi.MainResultAction
import com.example.weather.presenter.main.mvi.MainViewState
import com.example.weather.utils.error.ErrorMVI
import com.example.weather.utils.models.toEvent
import com.example.weather.utils.mvi.BaseViewModel
import kotlinx.coroutines.Dispatchers


class MainViewModel(app: Application, private val interactor: MainInteractor) :
    BaseViewModel<MainAction, MainViewState, MainResultAction>(app, MainViewState()) {

    override fun handleNewAction(action: MainAction) =
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            when (action) {
                is MainAction.LoadWeather -> {
                    emit(MainResultAction.Loading())
                    emitSource(interactor.getWeather())
                }
                is MainAction.UpdateTime -> {
                    emit(MainResultAction.UpdateTime)
                }
            }
        }

    override fun reduceNewViewState(
        currentState: MainViewState,
        result: MainResultAction
    ): MainViewState {
        return when (result) {
            is MainResultAction.Loading -> currentState.copy(
                isLoading = true
            )
            is MainResultAction.SuccessEmpty -> currentState.copy(
                isLoading = result.isLoading
            )
            is MainResultAction.Nothing -> currentState
            is MainResultAction.Error -> currentState.copy(
                isLoading = false,
                error = ErrorMVI.create(context, error = result.networkError).toEvent()
            )
            is MainResultAction.Success -> currentState.copy(
                isLoading = result.isLoading,
                data = result.data.toEvent(),
                timeOffset = result.data?.current?.timeOffset ?: 0,
                time = dayAndTime(offsetSec = result.data?.current?.timeOffset ?: 0)
            )
            is MainResultAction.UpdateTime -> currentState.copy(
                isLoading = false,
                time = dayAndTime(offsetSec = currentState.timeOffset)
            )
        }
    }
}
