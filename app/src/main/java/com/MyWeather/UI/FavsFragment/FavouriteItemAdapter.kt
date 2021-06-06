package com.MyWeather.UI.FavsFragment

import android.content.Context
import android.location.Geocoder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.MyWeather.data.DB.FavWeatherDao
import com.MyWeather.data.DB.ForecastDatabase
import com.MyWeather.data.NetworkData.Response.FavWeatherResponse
import com.MyWeather.myweather.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.favs_weather_item.view.*
import kotlinx.android.synthetic.main.favs_weather_item.view.imageView_alarm
import kotlinx.android.synthetic.main.favs_weather_item.view.textView_datealarm2
import kotlinx.android.synthetic.main.favs_weather_item.view.textView_temperature
import java.util.*


class FavouriteItemAdapter(
    //private val itemList: MutableList<FavouriteWeatherItem>,
    var favlist: List<FavWeatherResponse>,
    var context: Context,
    val listener: onItemClick
) :

    RecyclerView.Adapter<FavouriteItemAdapter.ItemViewHolder>() {

    var UNITSLOCAL = "metric"

    var database : FavWeatherDao = ForecastDatabase.buildDB(context).getFavDaoInstance()
    //var favlist :MutableList<FavWeatherResponse> = ArrayList()


    // lateinit var onItemClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.favs_weather_item,
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

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = favlist[position]
       // Log.i("FAVVVVC",currentItem.toString())


        var tempAbbreviation = if (UNITSLOCAL.equals("metric")) "°C" else "°F"
        var geocoder =
            Geocoder(context, Locale.getDefault())
        var city = geocoder.getFromLocation(currentItem.lon, currentItem.lat, 1)
        Log.i("CITTTTYYYY", city.toString())
        var cityName = if (equals(null)) city[0].featureName else city[0].adminArea
        Glide.with(context)
            .load("https://openweathermap.org/img/wn/${currentItem.current.weather[0].icon}@2x.png")
            .into(holder.imageView)
        holder.textViewcondition.text = currentItem.current.weather[0].description
        holder.textViewCity.text = cityName
        holder.textViewtemp.text =
            if (UNITSLOCAL.equals("metric")) "${currentItem.current.temp}$tempAbbreviation"
            else "${((currentItem.current.temp * 9 / 5) + 32).toFloat()} $tempAbbreviation"
    }



    fun removeItem(viewHolder: ItemViewHolder) {
        //favlist.removeAt(viewHolder.adapterPosition)
        notifyItemRemoved(viewHolder.adapterPosition)

//        var favWeatherResponse = itemList[viewHolder.adapterPosition]


    }

   // fun getFavoriteList(): MutableList<FavouriteWeatherItem> = itemList


    fun getItemByVH(viewHolder: RecyclerView.ViewHolder): FavWeatherResponse {
        return favlist[viewHolder.adapterPosition]
    }

//    fun itemfav(position: Int): FavouriteWeatherItem {
//        return favlist[position]
//    }

//    fun setOnItemClick( onItemClickListener: OnItemClickListener){
//        this.onItemClickListener = onItemClickListener
//
//    }
    fun setFavs(favList:List<FavWeatherResponse>){
    this.favlist=favlist
    notifyDataSetChanged()
}


    override fun getItemCount() = favlist.size


    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {


        val imageView = itemView.imageView_alarm
        val textViewCity = itemView.textView_datealarm
        val textViewcondition = itemView.textView_datealarm2
        val textViewtemp = itemView.textView_temperature

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                listener.onItemClick(pos)
            }
        }


    }

    interface onItemClick {
        fun onItemClick(pos: Int)
    }
}