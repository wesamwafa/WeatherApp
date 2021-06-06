package com.MyWeather.data.DB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.MyWeather.data.DB.Entity.Alarms
import com.MyWeather.data.DB.Entity.Current
import com.MyWeather.data.NetworkData.Response.CurrentWeatherResponse
import com.MyWeather.data.NetworkData.Response.FavWeatherResponse


@Database(entities = [CurrentWeatherResponse::class,FavWeatherResponse::class,Alarms::class],version = 6)
@TypeConverters(Converters::class)

abstract class ForecastDatabase :RoomDatabase() {

    abstract fun getDaoInstance():CurrentWeatherResponseDao
    abstract fun getFavDaoInstance():FavWeatherDao
    abstract fun getAlarmDaoInstance():AlarmsDao


    companion object{

        @Volatile private var instance:ForecastDatabase? = null
        private val LOCK = Any()
        operator fun invoke (context: Context) = instance?: synchronized(LOCK){
            instance?: buildDB(context).also { instance=it }
        }
        fun buildDB(context: Context)=Room.databaseBuilder(context.applicationContext,ForecastDatabase::class.java
                ,"forecast.db")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }
}