package com.MyWeather.data.DB.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Alarm_table")
data class Alarms (
    @PrimaryKey(autoGenerate = true)
    var id: Int? =null,
    var time : Long,
    var alarmid : Int

)
{

}
