package com.MyWeather.data.DB

import androidx.lifecycle.LiveData
import androidx.room.*

import com.MyWeather.data.DB.Entity.Alarms

@Dao
interface AlarmsDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert( alarm: Alarms)

    @Query("select * from Alarm_table where id = id")
    fun getAlarms(): LiveData<Alarms>

    @Query("SELECT * FROM Alarm_table")
    fun getListOfAlarms(): LiveData<MutableList<Alarms>>

    @Query("DELETE FROM alarm_table")
    fun deleteAllData()

    @Delete()
    fun deleteAlarmItem(alarm : Alarms)
}

