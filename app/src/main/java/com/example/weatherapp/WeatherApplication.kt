package com.example.weatherapp

import android.app.Application
import com.example.weatherapp.di.appModule
import com.facebook.drawee.backends.pipeline.Fresco
import org.koin.core.context.startKoin

class WeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(appModule)
        }
        Fresco.initialize(this)
    }
}