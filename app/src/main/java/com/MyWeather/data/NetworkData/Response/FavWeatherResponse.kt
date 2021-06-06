package com.MyWeather.data.NetworkData.Response

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.MyWeather.data.DB.Entity.Current
import com.MyWeather.data.DB.Entity.Daily
import com.MyWeather.data.DB.Entity.Hourly


@Entity(tableName = "fav_weather_response")

data class FavWeatherResponse(
        @PrimaryKey(autoGenerate = true)
        var id: Int? = null,
        @Embedded(prefix = "current_")
        val current: Current,
        val lat: Double,
        val lon: Double,
        val hourly: List<Hourly>,
        val daily: List<Daily>
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readValue(Int::class.java.classLoader) as? Int,
                parcel.readParcelable<Current>(Current::class.java.classLoader) as Current,
                parcel.readDouble(),
                parcel.readDouble(),
                TODO("hourly"),
                TODO("daily")
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeValue(id)
                parcel.writeParcelable(current, flags)
                parcel.writeDouble(lat)
                parcel.writeDouble(lon)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<FavWeatherResponse> {
                override fun createFromParcel(parcel: Parcel): FavWeatherResponse {
                        return FavWeatherResponse(parcel)
                }

                override fun newArray(size: Int): Array<FavWeatherResponse?> {
                        return arrayOfNulls(size)
                }
        }
}

