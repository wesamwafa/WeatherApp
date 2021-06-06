package com.MyWeather.data.DB.Entity


import android.os.Parcel
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
//const val CURRENT_WEATHER_ID = 0
//@Entity(tableName = "Current_weather")

data class  Current(
        val clouds: Int=0,
        val dt: Int=0,
        @SerializedName("feels_like")
        val feelsLike: Double=0.0,
        val humidity: Int=0,
        val pressure: Int=0,
        val temp: Double=0.0,
        //@Embedded(prefix = "weather_")
        val weather: List<Weather>,
        @SerializedName("wind_speed")
        val windSpeed: Double=0.0
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.readInt(),
                parcel.readDouble(),
                parcel.readInt(),
                parcel.readInt(),
                parcel.readDouble(),
                TODO("weather"),
                parcel.readDouble()
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeInt(clouds)
                parcel.writeInt(dt)
                parcel.writeDouble(feelsLike)
                parcel.writeInt(humidity)
                parcel.writeInt(pressure)
                parcel.writeDouble(temp)
                parcel.writeDouble(windSpeed)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<Current> {
                override fun createFromParcel(parcel: Parcel): Current {
                        return Current(parcel)
                }

                override fun newArray(size: Int): Array<Current?> {
                        return arrayOfNulls(size)
                }
        }
}
//{@PrimaryKey(autoGenerate = false)
//var id:Int = CURRENT_WEATHER_ID
//
//}



