package com.example.weather.data.repository.main

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.weather.data.db.city.City
import com.example.weather.data.db.weather.WeatherEntity
import com.example.weather.data.db.WeatherDataBase
import com.example.weather.data.network.ApiService
import com.example.weather.data.service.Locator
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
            NetworkBoundResource<HourlyWeather, List<WeatherEntity>?, MainResultAction>() {
            override suspend fun networkRequest(): HourlyWeather? {
                val cityEntity = safeCacheCall({ db.cityDao.getCurrentCity() })
                if (cityEntity != null)
                    return api.getWeatherCityByLocationHourlyForecast(
                        cityEntity.lat,
                        cityEntity.lon
                    )

                val location = locator.blockingGetLocation()
                if (location.securityError) return null
                if (location.locationIsNotAvailableNow) return null
                safeCacheCall({ location.city?.let { db.cityDao.saveCurrentCity(city = it) } })
                return api.getWeatherCityByLocationHourlyForecast(
                    location.city?.lat,
                    location.city?.lon
                )
            }

            override suspend fun retrieveCache(): List<WeatherEntity>? {
                val cityName = db.cityDao.getCurrentCityName()
                cityName ?: return null
                return db.weatherDao.getCurrentHourlyForecast(cityName = cityName)
            }

            override suspend fun saveCache(networkObject: HourlyWeather) {
                val cityName = db.cityDao.getCurrentCityName()
                networkObject.hourly?.mapNotNull {
                    WeatherEntity.createHourlyWeather(it, cityName)
                }?.let { hourlyList -> db.weatherDao.saveListWeather(hourlyList) }
            }

            override fun mapToResultAction(cache: List<WeatherEntity>?): MainResultAction {
                return MainResultAction.Loading
            }

            override fun mapErrorToResultAction(error: ApiResult.NetworkError?): MainResultAction {
                return MainResultAction.SomeAction()
            }

        }.result.asLiveData(Dispatchers.IO)
    }

    override fun getCityByName(city: String): LiveData<MainResultAction> {
        return object : NetworkBoundResource<HourlyWeather, Any, MainResultAction>() {

            override suspend fun networkRequest(): HourlyWeather? {
                val response = api.getWeatherCity(city) // лишний запрос чтобы достать данные city
                val cityEntity = City.create(response.city)
                db.cityDao.saveCity(cityEntity)
                return api.getWeatherCityByLocationHourlyForecast(cityEntity.lon, cityEntity.lat)
            }

            override suspend fun retrieveCache(): Any? {
                return Any()
            }

            override suspend fun saveCache(networkObject: HourlyWeather) {
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