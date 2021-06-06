package com.MyWeather.UI.Weather

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import androidx.work.WorkManager
import com.MyWeather.data.repository.ForecastRepository
import com.MyWeather.data.repository.ForecastRepositoryImpl
import com.MyWeather.internal.lazyDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CurrentWeatherViewModel(private val forecastRepository: ForecastRepository) : ViewModel() {


    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather()
    }


}





