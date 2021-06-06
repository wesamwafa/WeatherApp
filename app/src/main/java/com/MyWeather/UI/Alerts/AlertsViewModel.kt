package com.MyWeather.UI.Alerts

import androidx.lifecycle.ViewModel
import com.MyWeather.data.DB.Entity.Alarms

import com.MyWeather.data.repository.ForecastRepository
import com.MyWeather.internal.lazyDeferred

class AlertsViewModel(private val forecastRepository: ForecastRepository) : ViewModel() {

    val alarms = forecastRepository.getAlarms()

    fun alarmsdelete(alarms: Alarms){
        forecastRepository.deleteAlarm(alarms)
    }

    fun addAlarm(alarm :Alarms){
        forecastRepository.addAlarm(alarm)
    }
    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather()
    }




}