package com.example.weather.di

import com.example.weather.data.db.WeatherDataBase
import com.example.weather.data.network.ApiService
import com.example.weather.data.network.getApi
import com.example.weather.data.network.getClient
import com.example.weather.data.network.getRetrofitBuilder
import com.example.weather.data.repository.main.MainRepository
import com.example.weather.data.repository.main.MainRepositoryImpl
import com.example.weather.domain.main.MainInteractor
import com.example.weather.domain.main.MainInteractorImpl
import com.example.weather.presenter.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val mainModule = module {
    viewModel { MainViewModel(get(), get()) }
    factory<MainInteractor> { MainInteractorImpl(get()) }
    factory<MainRepository> { MainRepositoryImpl(get(),get()) }
    factory { getApi(get(), get()) }
    factory { getRetrofitBuilder(get()) }
    factory { getClient() }
    factory { WeatherDataBase.create(get()) }
}