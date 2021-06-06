package com.MyWeather.UI.Alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.MyWeather.data.repository.ForecastRepository


class AlertsViewModelFactory (private val forecastRepository: ForecastRepository)

    : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AlertsViewModel(forecastRepository) as T
    }


}
