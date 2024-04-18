package com.example.weatherapp.view.util

sealed class LoadingState {
    class Loading: LoadingState()
    class Success: LoadingState()
    class Error(val message: String): LoadingState()
}