package com.example.weatherapp.repo

import com.google.gson.annotations.SerializedName

data class ForecastWeatherInfo(
    @SerializedName("current") val currentWeatherInfo: CurrentWeatherInfo,
    @SerializedName("daily") val dailyList: List<ForecastOneDayInfo>
)

data class CurrentWeatherInfo(
    @SerializedName("temp") val temperature: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("weather") val weather: List<Weather>,
    @SerializedName("humidity") val humidity: Int
)

data class ForecastOneDayInfo(
    @SerializedName("temp") val tempInfo: TempInfo,
    @SerializedName("weather") val weather: List<Weather>
)

data class TempInfo(
    @SerializedName("day") val day: Double,
    @SerializedName("night") val night: Double
)

data class Weather(
    @SerializedName("icon") val icon: String,
    @SerializedName("main") val main: String,
    @SerializedName("description") val description: String
)
