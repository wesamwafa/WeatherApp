package com.MyWeather.data.NetworkData.Response


import androidx.annotation.Nullable
import com.MyWeather.data.DB.Entity.Alerts
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.MyWeather.data.DB.Entity.Current
import com.MyWeather.data.DB.Entity.Daily
import com.MyWeather.data.DB.Entity.Hourly
import com.google.gson.annotations.SerializedName

const val CURRENTWEATHERRESPONSEID =0
@Entity(tableName = "current_weather_response")
data class CurrentWeatherResponse(
        @Embedded(prefix="current_")
        val current: Current,
        val hourly: List<Hourly>,
        val daily: List<Daily>,
        var alerts : List<Alerts>?,
        val lat: Double,
        val lon: Double,
        val timezone: String,
        @SerializedName("timezone_offset")
        val timezoneOffset: Int
//        ,
//        val alerts : List<Alerts>
)

{@PrimaryKey(autoGenerate = false)
var id:Int = CURRENTWEATHERRESPONSEID

}
