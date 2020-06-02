package com.example.weather.presenter.city

import android.app.Application
import androidx.lifecycle.liveData
import com.example.weather.domain.city.CityInteractor
import com.example.weather.utils.error.ErrorMVI
import com.example.weather.utils.models.toEvent
import com.example.weather.utils.mvi.BaseViewModel
import kotlinx.coroutines.Dispatchers


class CityViewModel(app: Application, private val interactor: CityInteractor) :
    BaseViewModel<CityAction, CityViewState, CityResultAction>(app, CityViewState()) {

    override fun handleNewAction(action: CityAction) = liveData(Dispatchers.IO) {
        when (action) {
            is CityAction.LoadByQuery -> {
                emit(CityResultAction.Loading)
                emitSource(interactor.loadByQuery(action.q))
            }
            is CityAction.SaveCity -> {
                emit(interactor.saveCity(action.city))
            }
            is CityAction.LoadCityFromDB -> {
                emit(CityResultAction.Loading)
                emit(interactor.loadCityFromCache())
            }
            is CityAction.DeleteCity -> {
                interactor.deleteCity(action.deletedCity.countryAndPostCode)
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
            is CityResultAction.CitySaved -> currentViewState.copy(
                isLoading = false,
                data = result.cachedCity.toEvent()
            )
            is CityResultAction.SuccessEmpty -> currentViewState.copy(isLoading = false) //todo
        }
    }
}
