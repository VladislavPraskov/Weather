package com.example.weather.data.repository.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.weather.data.db.city.CityEntity
import com.example.weather.data.db.WeatherDataBase
import com.example.weather.data.network.ApiService
import com.example.weather.data.service.Locator
import com.example.weather.models.WeatherUI
import com.example.weather.models.main.HourlyWeather
import com.example.weather.presenter.main.mvi.MainResultAction
import com.example.weather.utils.network.ApiResult
import com.example.weather.utils.network.NetworkBoundResource
import com.example.weather.utils.network.safeCacheCall
import kotlinx.coroutines.Dispatchers

class MainRepositoryImpl(
    val api: ApiService,
    val db: WeatherDataBase,
    val locator: Locator
) : MainRepository {

    override fun getCurrentCity(): LiveData<MainResultAction> {
        return object :
            NetworkBoundResource<HourlyWeather, WeatherUI?, MainResultAction>() {
            override suspend fun networkRequest(): HourlyWeather? {
                val cityEntity = safeCacheCall({ db.cityDao.getCurrentCity() })
                if (cityEntity != null)
                    return api.getWeatherCityByLocationHourlyForecast(
                        cityEntity.lat,
                        cityEntity.lon
                    )

                val location = locator.blockingGetLocation()
                if (location.securityError) return null //todo
                if (location.locationIsNotAvailableNow) return null //todo
                safeCacheCall({ location.city?.let { db.cityDao.saveCurrentCity(cityEntity = it) } })
                return api.getWeatherCityByLocationHourlyForecast(
                    location.city?.lat,
                    location.city?.lon
                )
            }

            override suspend fun retrieveCache(): WeatherUI? {
                return db.sharedDao.getWeather()
            }

            override suspend fun saveCache(networkObject: HourlyWeather) {
                db.sharedDao.saveWeather(networkObject)
            }

            override fun mapToResultAction(cache: WeatherUI?, isFirst: Boolean): MainResultAction {
                return MainResultAction.getSuccessOrEmpty(cache, isFirst)
            }

            override fun mapErrorToResultAction(error: ApiResult.NetworkError?): MainResultAction {
                return MainResultAction.Error(error)
            }

        }.result.asLiveData(Dispatchers.IO)
    }
}