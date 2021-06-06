package com.MyWeather.UI.FavsFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.MyWeather.data.repository.ForecastRepository

@Suppress("UNCHECKED_CAST")
class FavsViewModelFactory(private val forecastRepository: ForecastRepository)

    : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FavsViewModel(forecastRepository) as T
    }


}