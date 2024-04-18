package com.example.weatherapp.view

import android.graphics.PointF
import androidx.lifecycle.ViewModel
import androidx.lifecycle.*
import com.example.weatherapp.repo.WeatherRepository
import com.example.weatherapp.usecase.WeatherInfoUseCase
import com.example.weatherapp.view.util.CalendarWeek
import com.example.weatherapp.view.util.ForecastAdapter
import com.example.weatherapp.view.util.ForecastItem
import com.example.weatherapp.view.util.LoadingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import java.util.Calendar

class WeatherViewModel(private val weatherInfoUseCase: WeatherInfoUseCase) : ViewModel() {

    private val _weatherState: MutableStateFlow<WeatherInfo?> = MutableStateFlow(null)
    val weatherState: StateFlow<WeatherInfo?> = _weatherState

    private val _loadingState: MutableStateFlow<LoadingState> = MutableStateFlow(LoadingState.Loading())
    val loadingState: StateFlow<LoadingState> = _loadingState

    private val calendar = Calendar.getInstance()

    init {
        getWeatherPoint(PointF(40.1792f, 44.4991f)) // Yerevan weather
    }

    fun getWeather(city: String) {
        viewModelScope.launch {
            cities[city]?.let {
                getWeatherPoint(it)
            } ?: run {
                _loadingState.emit(LoadingState.Error("No such city in the list!"))
            }
        }
    }

    private fun getWeatherPoint(pointF: PointF) {
        viewModelScope.launch {
            _loadingState.emit(LoadingState.Loading())
            try {
                weatherInfoUseCase.getWeatherInfo(pointF).collect {
                    val list = it.dailyList
                    val forecastItems =
                        list.subList(1, list.size).map { forecastOneDayInfo ->
                            ForecastInfo(
                                temp = Temp(
                                    day = forecastOneDayInfo.tempInfo.day.toInt()
                                        .toString() + DEGREE_SYMBOL,
                                    night = forecastOneDayInfo.tempInfo.night.toInt()
                                        .toString() + DEGREE_SYMBOL
                                ),
                                weather = Weather(
                                    icon = "https://openweathermap.org/img/wn/${forecastOneDayInfo.weather[0].icon}@2x.png",
                                    main = forecastOneDayInfo.weather[0].main,
                                    desc = forecastOneDayInfo.weather[0].description
                                )
                            )
                        }
                    _weatherState.emit(
                        WeatherInfo(
                            currentWeatherInfo = CurrentWeatherInfo(
                                currentTemp = it.currentWeatherInfo.temperature.toInt()
                                    .toString() + DEGREE_SYMBOL,
                                feelsLike = "Feel like ${it.currentWeatherInfo.feelsLike.toInt()}$DEGREE_SYMBOL",
                                humidity = "Humidity - ${it.currentWeatherInfo.humidity}",
                                weather = Weather(
                                    icon = "https://openweathermap.org/img/wn/${it.currentWeatherInfo.weather[0].icon}@4x.png",
                                    main = it.currentWeatherInfo.weather[0].main,
                                    desc = it.currentWeatherInfo.weather[0].description
                                )
                            ),
                            forecastInfo = forecastItems
                        )
                    )
                    _loadingState.emit(LoadingState.Success())
                }
            } catch (e: Exception) {
                if (e is UnknownHostException) {
                    _loadingState.emit(LoadingState.Error("No internet!"))
                } else {
                    _loadingState.emit(LoadingState.Error("Unknown error"))
                }
            }
        }
    }

    fun mapWithTitles(list: List<ForecastInfo>): List<ForecastItem> {
        val today = getDayOfWeekString(calendar.get(Calendar.DAY_OF_WEEK))
        val days = CalendarWeek().getNamesStartingFrom(today)
        return list.mapIndexed { index, forecastInfo ->
            ForecastItem(forecastInfo.temp.day, forecastInfo.temp.night, forecastInfo.weather.icon, forecastInfo.weather.main, days[index])
        }
    }

    private fun getDayOfWeekString(day: Int): String {
        return when (day) {
            Calendar.TUESDAY -> CalendarWeek.TUESDAY
            Calendar.WEDNESDAY -> CalendarWeek.WEDNESDAY
            Calendar.THURSDAY -> CalendarWeek.THURSDAY
            Calendar.FRIDAY -> CalendarWeek.FRIDAY
            Calendar.SATURDAY -> CalendarWeek.SATURDAY
            Calendar.SUNDAY -> CalendarWeek.SUNDAY
            else -> CalendarWeek.MONDAY
        }
    }

    companion object {
        const val DEGREE_SYMBOL = "°C"
        val cities = mapOf(
            "New York City" to PointF(40.7128f, -74.0060f),
            "London" to PointF(51.5074f, -0.1278f),
            "Paris" to PointF(48.8566f, 2.3522f),
            "Tokyo" to PointF(35.6895f, 139.6917f),
            "Sydney" to PointF(-33.8688f, 151.2093f),
            "Beijing" to PointF(39.9042f, 116.4074f),
            "Moscow" to PointF(55.7558f, 37.6176f),
            "Dubai" to PointF(25.276987f, 55.296249f),
            "Rio de Janeiro" to PointF(-22.9068f, -43.1729f),
            "Cape Town" to PointF(-33.9249f, 18.4241f),
            "Yerevan" to PointF(40.1792f, 44.4991f)
        )
    }
}

class WeatherInfo(
    val currentWeatherInfo: CurrentWeatherInfo,
    val forecastInfo: List<ForecastInfo>
)

class CurrentWeatherInfo(
    val currentTemp: String,
    val feelsLike: String,
    val humidity: String,
    val weather: Weather
)

class Temp(
    val day: String,
    val night: String
)

class Weather(
    val icon: String,
    val main: String,
    val desc: String
)

class ForecastInfo(
    val temp: Temp,
    val weather: Weather
)
