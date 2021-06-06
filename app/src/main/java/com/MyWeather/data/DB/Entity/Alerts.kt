package com.MyWeather.data.DB.Entity

data class Alerts(

    val sender_name: String,
    val event: String,
    val start: Int,
    val end: Int,
    val description: String
)