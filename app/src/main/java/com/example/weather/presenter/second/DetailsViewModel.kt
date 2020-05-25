package com.example.weather.presenter.second

import android.app.Application
import androidx.lifecycle.liveData
import com.example.weather.domain.main.MainInteractor
import com.example.weather.presenter.second.mvi.DetailsAction
import com.example.weather.presenter.second.mvi.DetailsResultAction
import com.example.weather.presenter.second.mvi.DetailsViewState
import com.example.weather.utils.mvi.BaseViewModel


class DetailsViewModel(app: Application, val interactor: MainInteractor) :
    BaseViewModel<DetailsAction, DetailsViewState, DetailsResultAction>(app,
        DetailsViewState()
    ) {

    override fun handleNewAction(action: DetailsAction) = liveData {
        when (action) {
            is DetailsAction.SomeAction -> {
                emit(DetailsResultAction.Loading)
            }
            is DetailsAction.SomeAction2 -> {
                emit(DetailsResultAction.SomeAction())
            }
        }
    }

    override fun reduceNewViewState(
        currentViewState: DetailsViewState,
        result: DetailsResultAction
    ): DetailsViewState {
        return when (result) {
            is DetailsResultAction.SomeAction -> currentViewState.copy(isLoading = true)
            is DetailsResultAction.Loading -> currentViewState.copy(isLoading = true)
        }
    }
}
