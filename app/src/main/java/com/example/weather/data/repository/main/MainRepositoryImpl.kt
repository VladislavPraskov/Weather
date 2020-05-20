package com.example.weather.data.repository.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.weather.data.db.WeatherDataBase
import com.example.weather.data.network.ApiService
import com.example.weather.presenter.main.MainResultAction
import com.example.weather.utils.network.ApiResult
import com.example.weather.utils.network.NetworkBoundResource
import kotlinx.coroutines.Dispatchers

class MainRepositoryImpl(val api: ApiService, val db: WeatherDataBase) : MainRepository {

   override fun getWeatherCity(): LiveData<MainResultAction> {
        return object : NetworkBoundResource<Any, Any, MainResultAction>() {
            override suspend fun networkRequest(): Any? {
                return api.getWeatherCity()
            }

            override suspend fun retrieveCache(): Any? {
                return Any()
            }

            override suspend fun saveCache(networkObject: Any) {
            }

            override fun mapToResultAction(cache: Any?): MainResultAction {
                return MainResultAction.Loading
            }

            override fun mapErrorToResultAction(error: ApiResult.NetworkError?): MainResultAction {
                return MainResultAction.SomeAction()
            }

        }.result.asLiveData(Dispatchers.IO)
    }

}