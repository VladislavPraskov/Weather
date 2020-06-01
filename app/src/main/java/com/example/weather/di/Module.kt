package com.example.weather.di

import androidx.preference.PreferenceManager
import com.example.weather.data.db.WeatherDataBase
import com.example.weather.data.network.*
import com.example.weather.data.network.RetrofitService.BASE_CITY_URL
import com.example.weather.data.network.RetrofitService.BASE_WEATHER_URL
import com.example.weather.data.repository.city.CityRepository
import com.example.weather.data.repository.city.CityRepositoryImpl
import com.example.weather.data.repository.main.MainRepository
import com.example.weather.data.repository.main.MainRepositoryImpl
import com.example.weather.data.service.Locator
import com.example.weather.domain.city.CityInteractor
import com.example.weather.domain.city.CityInteractorImpl
import com.example.weather.domain.main.MainInteractor
import com.example.weather.domain.main.MainInteractorImpl
import com.example.weather.presenter.city.CityViewModel
import com.example.weather.presenter.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mainModule = module {
    viewModel { MainViewModel(get(), get()) }
    viewModel { CityViewModel(get(), get()) }

    factory<MainInteractor> { MainInteractorImpl(get()) }
    factory<CityInteractor> { CityInteractorImpl(get()) }

    factory<MainRepository> { MainRepositoryImpl(get(), get(), get()) }
    factory<CityRepository> { CityRepositoryImpl(get(), get()) }

    factory(named("weatherI")) { MainInterceptor("appid", KeysGitIgnore.WEATHER_KEY) }
    factory(named("cityI")) { MainInterceptor("username", KeysGitIgnore.CITY_KEY) }
    factory(named("weatherC")) { getClient(get(named("weatherI"))) }
    factory(named("cityC")) { getClient(get(named("cityI"))) }
    factory(named("weatherR")) { getRetrofitBuilder(BASE_WEATHER_URL) }
    factory(named("cityR")) { getRetrofitBuilder(BASE_CITY_URL) }
    factory { getWeatherApi(get(named("weatherR")), get(named("weatherC"))) }
    factory { getCityApi(get(named("cityR")), get(named("cityC"))) }

    factory { PreferenceManager.getDefaultSharedPreferences(get()) }
    single { WeatherDataBase.create(get()) }
    single { Locator(get()) }
}