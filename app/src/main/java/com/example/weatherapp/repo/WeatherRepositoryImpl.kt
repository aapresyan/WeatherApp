package com.example.weatherapp.repo

import android.graphics.PointF
import android.util.Log
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherRepositoryImpl : WeatherRepository {

    private val apiService = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WeatherApi::class.java)

    override suspend fun getWeatherInfo(point: PointF): Flow<ForecastWeatherInfo> {
        return flow {
            emit(apiService.getWeather(point.x.toDouble(), point.y.toDouble(), "d4cf2f3c3f14012ab2ba541b40d5da53"))
        }
    }
}