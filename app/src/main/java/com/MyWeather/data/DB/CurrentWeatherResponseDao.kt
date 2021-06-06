package com.MyWeather.data.DB


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.MyWeather.data.DB.Entity.Current
import com.MyWeather.data.NetworkData.Response.CURRENTWEATHERRESPONSEID
import com.MyWeather.data.NetworkData.Response.CurrentWeatherResponse

@Dao
interface CurrentWeatherResponseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(currentWeather: CurrentWeatherResponse)

    @Query("select * from current_weather_response where id = $CURRENTWEATHERRESPONSEID")
    fun getWeather(): LiveData<CurrentWeatherResponse>
}
