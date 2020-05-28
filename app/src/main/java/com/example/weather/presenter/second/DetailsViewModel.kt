package com.example.weather.presenter.second

import android.app.Application
import androidx.lifecycle.liveData
import com.example.weather.domain.main.MainInteractor
import com.example.weather.presenter.second.mvi.DetailsAction
import com.example.weather.presenter.second.mvi.DetailsResultAction
import com.example.weather.presenter.second.mvi.DetailsViewState
import com.example.weather.utils.mvi.BaseViewModel


class DetailsViewModel(app: Application, val interactor: MainInteractor) :
    BaseViewModel<DetailsAction, DetailsViewState, DetailsResultAction>(
        app,
        DetailsViewState()
    ) {

    override fun handleNewAction(action: DetailsAction) = liveData<DetailsResultAction> {
        when (action) {
            is DetailsAction.CurrentAndDailyForecast -> {
//                emit(interactor.getCurrentCity())
            }
            is DetailsAction.SomeAction2 -> {
                emit(DetailsResultAction.CurrentAndDailyForecast)
            }
        }
    }

    override fun reduceNewViewState(
        currentViewState: DetailsViewState,
        result: DetailsResultAction
    ): DetailsViewState {
        return when (result) {
            is DetailsResultAction.CurrentAndDailyForecast -> currentViewState.copy(isLoading = true)
        }
    }
}
