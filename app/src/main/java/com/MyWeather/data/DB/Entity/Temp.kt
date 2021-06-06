package com.MyWeather.data.DB.Entity


data class Temp(
    val day: Double,
    val eve: Double,
    var max: Double,
    val min: Double,
    val morn: Double,
    val night: Double
)