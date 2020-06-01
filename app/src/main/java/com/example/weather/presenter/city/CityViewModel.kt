package com.example.weather.presenter.city

import android.app.Application
import androidx.lifecycle.liveData
import com.example.weather.domain.city.CityInteractor
import com.example.weather.utils.error.ErrorMVI
import com.example.weather.utils.models.toEvent
import com.example.weather.utils.mvi.BaseViewModel


class CityViewModel(app: Application, private val interactor: CityInteractor) :
    BaseViewModel<CityAction, CityViewState, CityResultAction>(app, CityViewState()) {

    override fun handleNewAction(action: CityAction) = liveData {
        when (action) {
            is CityAction.LoadByQuery -> {
                emit(CityResultAction.Loading)
                emitSource(interactor.loadByQuery(action.q))
            }
        }
    }

    override fun reduceNewViewState(
        currentViewState: CityViewState,
        result: CityResultAction
    ): CityViewState {
        return when (result) {
            is CityResultAction.Loading -> {
                currentViewState.copy(isLoading = true)
            }
            CityResultAction.Nothing -> currentViewState.copy(isLoading = false)
            is CityResultAction.Error -> currentViewState.copy(
                isLoading = false,
                error = ErrorMVI.create(context, error = result.networkError)
            )
            is CityResultAction.Success -> currentViewState.copy(
                isLoading = false,
                data = result.data.toEvent()
            )
            CityResultAction.SuccessEmpty -> currentViewState.copy(isLoading = false)
        }
    }
}
