package com.example.weather.data.repository.city

import androidx.lifecycle.LiveData
import com.example.weather.data.db.WeatherDataBase
import com.example.weather.data.network.CityApiService
import com.example.weather.data.network.NetworkResourse
import com.example.weather.models.CityResponseModel
import com.example.weather.presenter.city.CityResultAction
import com.example.weather.utils.mapToCityUI


class CityRepositoryImpl(val db: WeatherDataBase, private val api: CityApiService) : CityRepository {

    override fun loadByQuery(q: String): LiveData<CityResultAction> {
        return NetworkResourse.create(
            apiCall = { api.getCityByQuery(q) },
            onSuccess = { success -> CityResultAction.getSuccessOrEmpty(mapToCityUI(success.value)) },
            onError = { CityResultAction.Loading }
        ).result
    }

}