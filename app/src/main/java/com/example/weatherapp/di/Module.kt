package com.example.weatherapp.di

import com.example.weatherapp.repo.WeatherRepository
import com.example.weatherapp.repo.WeatherRepositoryImpl
import com.example.weatherapp.usecase.WeatherInfoUseCase
import com.example.weatherapp.view.WeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<WeatherRepository> {
        WeatherRepositoryImpl()
    }

    single {
        WeatherInfoUseCase(get())
    }

    viewModel {
        WeatherViewModel(get())
    }
}