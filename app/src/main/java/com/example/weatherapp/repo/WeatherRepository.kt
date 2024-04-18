package com.example.weatherapp.repo

import android.graphics.PointF
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getWeatherInfo(pointF: PointF): Flow<ForecastWeatherInfo>
}