package com.example.weather.data.repository.city

import androidx.lifecycle.LiveData
import com.example.weather.data.db.WeatherDataBase
import com.example.weather.data.network.CityApiService
import com.example.weather.data.network.NetworkResourse
import com.example.weather.presenter.city.CityResultAction


class CityRepositoryImpl(val db: WeatherDataBase, val api: CityApiService) : CityRepository {

    override fun loadByQuery(q: String): LiveData<CityResultAction> {
        return NetworkResourse.create<Unit, CityResultAction>(
            apiCall = { api.getCityByQuery(q) },
            onSuccess = { success -> CityResultAction.Loading },
            onError = { CityResultAction.Loading }
        ).result
    }

}