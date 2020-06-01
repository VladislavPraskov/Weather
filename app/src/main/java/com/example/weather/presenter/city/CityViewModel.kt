package com.example.weather.presenter.city

import android.app.Application
import androidx.lifecycle.liveData
import com.example.weather.domain.city.CityInteractor
import com.example.weather.utils.mvi.BaseViewModel


class CityViewModel(app: Application, val interactor: CityInteractor) :
    BaseViewModel<CityAction, CityViewState, CityResultAction>(app, CityViewState()) {

    override fun handleNewAction(action: CityAction) = liveData<CityResultAction> {
        when (action) {
            is CityAction.LoadByQuery -> {
                emit(CityResultAction.Loading)
                emitSource(interactor.loadByQuery(action.q))
            }
            is CityAction.LoadCityFromDB -> {
                emit(CityResultAction.SomeAction())
            }
        }
    }

    override fun reduceNewViewState(
        currentViewState: CityViewState,
        result: CityResultAction
    ): CityViewState {
        return when (result) {
            is CityResultAction.SomeAction -> {
                currentViewState.copy()
            }
            is CityResultAction.Loading -> {
                currentViewState.copy(isLoading = true)
            }
        }
    }
}
