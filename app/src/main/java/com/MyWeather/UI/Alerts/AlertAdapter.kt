package com.MyWeather.UI.Alerts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.MyWeather.data.DB.Entity.Alarms
import com.MyWeather.data.DB.ForecastDatabase
import com.MyWeather.myweather.R
import kotlinx.android.synthetic.main.alert_item.view.*
import java.text.SimpleDateFormat
import java.util.*


class AlertAdapter(
    var alarmlist: List<Alarms>,
    var context: Context
) : RecyclerView.Adapter<AlertAdapter.ItemViewHolder>() {

    var database = ForecastDatabase.buildDB(context).getAlarmDaoInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertAdapter.ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.alert_item,
            parent, false
        )
        return ItemViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: AlertAdapter.ItemViewHolder, position: Int) {
        val currentItem = alarmlist[position]

        holder.imageViewalarm.setImageResource(R.drawable.ic_bell)
        holder.textViewdate.text=unixTimeStampToTime(currentItem.time)
        holder.textViewtime.text=unixTimeStampToDate(currentItem.time)
    }

    override fun getItemCount() = alarmlist.size

    fun getItemByVH(viewHolder: RecyclerView.ViewHolder): Alarms {
        return alarmlist[viewHolder.adapterPosition]
    }

    fun removeItem(viewHolder: AlertAdapter.ItemViewHolder) {
        //favlist.removeAt(viewHolder.adapterPosition)
        notifyItemRemoved(viewHolder.adapterPosition)
    }

        inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
            {

            val imageViewalarm = itemView.imageView_alarm
            val textViewtime = itemView.textView_datealarm
            val textViewdate = itemView.textView_datealarm2

        }
    fun unixTimeStampToDate(untixTimeStamp:Long):String{
    val dateFormat=SimpleDateFormat("dd/MM/yyyy")
    val date= Date()
    date.time=untixTimeStamp
    return dateFormat.format(date)}

    fun unixTimeStampToTime(untixTimeStamp: Long):String{
    val timeFormat= SimpleDateFormat("hh:mm a")
    val date= Date()
    date.time=untixTimeStamp
    return timeFormat.format(date)
}


    //@SuppressLint("SimpleDateFormat")
//@BindingAdapter("displayDate")
//public fun unixTimeStampToDate(textView: TextView,untixTimeStamp:Int):String{
//    val dateFormat=SimpleDateFormat("dd/MM/yyyy")
//    val date= Date()
//    date.time=untixTimeStamp.toLong()*1000
//    textView.text = dateFormat.format(date)
//    return dateFormat.format(date)
//}
///**
// * convert unix time to time
// */
//@SuppressLint("SimpleDateFormat")
//@BindingAdapter("displayTime")
//public fun unixTimeStampToTime(textView: TextView,untixTimeStamp:Int):String{
//    val timeFormat=SimpleDateFormat("HH:mm")
//    val date= Date()
//    date.time=untixTimeStamp.toLong()*1000
//    textView.text = timeFormat.format(date)
//    return timeFormat.format(date)
//}
    }


