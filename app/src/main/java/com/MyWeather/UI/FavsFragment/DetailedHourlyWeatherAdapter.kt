package com.MyWeather.UI.FavsFragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.MyWeather.myweather.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.detailedhourlyweatheritem.view.*


class DetailedHourlyWeatherAdapter(private val itemList: List<DetailedHourlyWeatherItem>, var context: Context) :
    RecyclerView.Adapter<DetailedHourlyWeatherAdapter.ItemViewHolder>() {

    var UNITSLOCAL = "metric"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.detailedhourlyweatheritem,
            parent, false
        )
        val preferences = PreferenceManager.getDefaultSharedPreferences(context?.applicationContext)
        var selectedName = preferences.getString("UNIT_SYSTEM", "METRIC")
        if (selectedName == "METRIC") {
            UNITSLOCAL = "metric"
        } else {
            UNITSLOCAL = "imperial"
        }

        return ItemViewHolder(itemView)
    }

    override fun getItemCount()= itemList.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val currentItem = itemList[position]
        var tempAbbreviation = if (UNITSLOCAL.equals("metric"))"°" else "°"

        Glide.with(context)
            .load("https://openweathermap.org/img/wn/${currentItem.image}@2x.png")
            .into(holder.imageView)
        //holder.imageView.setImageIcon(currentItem.image)
        holder.textViewHour.text = currentItem.hour
        holder.textViewtemp.text = if (UNITSLOCAL.equals("metric")) "${currentItem.temperature}$tempAbbreviation"
        else "${((currentItem.temperature*9/5)+32).toFloat()} $tempAbbreviation"

    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView = itemView.imageView_alarm
        val textViewHour = itemView.textView_hour
        val textViewtemp = itemView.textView_temperature

    }

}