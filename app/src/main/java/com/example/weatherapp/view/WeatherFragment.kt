package com.example.weatherapp.view

import android.R
import android.app.Activity
import android.content.Context
import android.graphics.PointF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.databinding.FragmentWeatherBinding
import com.example.weatherapp.view.util.CalendarWeek
import com.example.weatherapp.view.util.ForecastAdapter
import com.example.weatherapp.view.util.ForecastItem
import com.example.weatherapp.view.util.LoadingState
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar

class WeatherFragment : Fragment() {

    private val viewModel: WeatherViewModel by viewModel()
    private lateinit var binding: FragmentWeatherBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ArrayAdapter(requireContext(), R.layout.simple_dropdown_item_1line, WeatherViewModel.cities.keys.toList())
        binding.location.setAdapter(adapter)
        binding.location.setOnClickListener {
            binding.location.showDropDown()
        }

        binding.searchButton.setOnClickListener {
            viewModel.getWeather(binding.location.text.toString())
        }

        lifecycleScope.launch {
            viewModel.loadingState.collect {
                when (it) {
                    is LoadingState.Loading -> {
                        binding.loading.visibility = View.VISIBLE
                        hideKeyboard(requireActivity())
                    }
                    is LoadingState.Success -> {
                        binding.loading.visibility = View.GONE
                        hideKeyboard(requireActivity())
                    }
                    is LoadingState.Error -> {
                        binding.loading.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.weatherState.collect {
                it?.currentWeatherInfo?.let { currentWeatherInfo ->
                    binding.currentTemperature.text = currentWeatherInfo.currentTemp
                    binding.feelsLike.text = currentWeatherInfo.feelsLike
                    binding.humidity.text = currentWeatherInfo.humidity
                    binding.weatherDesc.text = currentWeatherInfo.weather.desc
                    binding.weatherImage.setImageURI(currentWeatherInfo.weather.icon)
                }

                it?.forecastInfo?.let { list ->
                    if (list.isNotEmpty()) {
                        binding.todaysTemperature.text =
                            "${list[0].temp.day} / ${list[0].temp.night}"
                        binding.todaysTemperature.visibility = View.VISIBLE
                        val adapter = ForecastAdapter(viewModel.mapWithTitles(list))
                        binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        binding.recyclerView.adapter = adapter
                    }
                }
            }
        }
    }

    private fun hideKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocus = activity.currentFocus
        if (currentFocus != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }
}