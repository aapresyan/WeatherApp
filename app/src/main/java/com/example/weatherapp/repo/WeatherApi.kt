package com.example.weatherapp.repo

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/3.0/onecall")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("appid") appid: String,
        @Query("units") units: String = "metric",
        @Query("exclude") exclude: String = "minutely,hourly,alerts"
    ): ForecastWeatherInfo
}