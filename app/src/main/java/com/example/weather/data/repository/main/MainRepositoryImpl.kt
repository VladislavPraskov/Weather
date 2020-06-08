package com.example.weather.data.repository.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.weather.data.db.WeatherDataBase
import com.example.weather.data.error.LocationNotAvailableException
import com.example.weather.data.error.LocationSecurityException
import com.example.weather.data.network.api.ApiService
import com.example.weather.data.service.Locator
import com.example.weather.models.WeatherUI
import com.example.weather.models.main.WeatherResponse
import com.example.weather.presenter.main.mvi.MainResultAction
import com.example.weather.utils.network.ApiResult
import com.example.weather.utils.network.NetworkBoundResource
import com.example.weather.utils.network.safeCacheCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.coroutineContext

class MainRepositoryImpl(
    val api: ApiService,
    val db: WeatherDataBase,
    val locator: Locator
) : MainRepository {
    var job = Job()

    override suspend fun getWeather(): LiveData<MainResultAction> {
        job.cancel()
        job = Job()
        return object :
            NetworkBoundResource<WeatherResponse, WeatherUI?, MainResultAction>() {
            override suspend fun networkRequest(): WeatherResponse? {
                val cityEntity = safeCacheCall({ db.cityDao.getCurrentCity() })

                if (cityEntity != null)
                    return api.getWeatherForecast(cityEntity.lat, cityEntity.lon)

                val location = locator.blockingGetLocation()
                if (location.securityError) throw LocationSecurityException()
                if (location.locationIsNotAvailableNow) throw LocationNotAvailableException()
                safeCacheCall({ location.city?.let { db.cityDao.saveCurrentCity(cityEntity = it) } })

                return api.getWeatherForecast(location.city?.lat, location.city?.lon)
            }

            override suspend fun retrieveCache(): WeatherUI? {
                return db.sharedDao.getWeather()
            }

            override suspend fun saveCache(networkObject: WeatherResponse) {
                db.sharedDao.saveWeather(networkObject)
            }

            override fun mapToResultAction(cache: WeatherUI?, isCache: Boolean): MainResultAction {
                return MainResultAction.getSuccessOrEmpty(cache, isCache)
            }

            override fun mapErrorToResultAction(error: ApiResult.NetworkError?): MainResultAction {
                return MainResultAction.Error(error)
            }

        }.result.asLiveData(job + Dispatchers.IO)
    }
}