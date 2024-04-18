package com.example.weatherapp.usecase

import android.graphics.PointF
import com.example.weatherapp.repo.WeatherRepository

class WeatherInfoUseCase(private val weatherRepository: WeatherRepository) {
    suspend fun getWeatherInfo(pointF: PointF) = weatherRepository.getWeatherInfo(pointF)
}