package com.MyWeather.UI.Weather

import java.text.SimpleDateFormat
import java.util.*


data class FutureWeatherItem(
    val image: String, val date: String, val condition: String, val temperature: Double

)


    private fun updateDate(strangeDate: Int): String {

        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val date = Date()
        date.time = strangeDate.toLong() * 1000

        return dateFormat.format(date)
    }
