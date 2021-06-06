package com.MyWeather.UI.Alerts

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.MyWeather.UI.Weather.ScopedFragment
import com.MyWeather.data.DB.Entity.Alarms
import com.MyWeather.myweather.R
import kotlinx.android.synthetic.main.alerts_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*

class AlertsFragment : ScopedFragment(), KodeinAware, DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    override val kodein by closestKodein()
    val viewModelFactory: AlertsViewModelFactory by instance()

    var day = 0
    var month: Int = 0
    var year: Int = 0
    var hour: Int = 0
    var minute: Int = 0
    var myDay = 1
    var myMonth: Int = 0
    var myYear: Int = 0
    var myHour: Int = 0
    var myMinute: Int = 0
    var ALERTDESCRIPTION = "No Dangerous Alerts"
    var ALERTTIMESTART = 0
    var ALERTTIMEEND = 0
    var ALERTSOUND = true
    private lateinit var alarmManager: AlarmManager
    private lateinit var viewModel: AlertsViewModel
    private lateinit var alertReceiver: AlertReceiver
    private lateinit var alertAdapter: AlertAdapter
    private lateinit var alarmsList: List<Alarms>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, viewModelFactory).get(AlertsViewModel::class.java)

        init()
        return inflater.inflate(R.layout.alerts_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        alarmManager =
            requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        datePicker.minDate = System.currentTimeMillis() - 1000



        floatingActionButtonAlarm.setOnClickListener {
//            val calendar: Calendar = Calendar.getInstance()
//            day = calendar.get(Calendar.DAY_OF_MONTH)
//            month = calendar.get(Calendar.MONTH)
//            year = calendar.get(Calendar.YEAR)
//            val datePickerDialog =
//                DatePickerDialog(requireContext(), this@AlertsFragment, year, month,day)
//            datePickerDialog.show()
            //Toast.makeText(requireContext(),"Alarm Set",Toast.LENGTH_LONG)

//            val datePickerDialog =
//                DatePickerDialog(requireContext(), this@AlertsFragment, year, month,day)
//            datePickerDialog.show()
//            val timePickerDialog=TimePickerDialog(context,this@AlertsFragment, hour,minute,false)
//            timePickerDialog.show()

            setAlarm(
                timePicker.hour,
                timePicker.minute,
                datePicker.dayOfMonth,
                datePicker.month,
                datePicker.year
            )


        }
        init()


    }

    fun init() {

        val preferences = PreferenceManager.getDefaultSharedPreferences(context?.applicationContext)
        var selectedalarm =
            preferences.getBoolean("WEATHER_ALARMS", true)
        ALERTSOUND = selectedalarm



        getAlertsFromCurrent()
        viewModel.alarms.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            alarmsList = it

            recycleViewAlerts.layoutManager =
                LinearLayoutManager(context/*,RecyclerView.HORIZONTAL,false*/)
            recycleViewAlerts.hasFixedSize()
            alertAdapter = AlertAdapter(alarmsList, requireContext())
            recycleViewAlerts.adapter = alertAdapter
            ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recycleViewAlerts)


        })
    }

    private fun setAlarm(hour: Int, min: Int, day: Int, month: Int, year: Int) {
        val intentA = Intent(context, AlertReceiver::class.java)
        intentA.putExtra("alert_description", ALERTDESCRIPTION)
        intentA.putExtra("alarm_type", ALERTSOUND)
        val r = Random()
        val randomReq = r.nextInt(99)
        val pendingIntentA = PendingIntent.getBroadcast(context, randomReq, intentA, 0)
        val calendar = Calendar.getInstance()
        println(hour)
        println(min)
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, min)
        calendar[Calendar.MONTH] = month
        calendar[Calendar.DATE] = day
        calendar[Calendar.YEAR] = year
        calendar[Calendar.SECOND] = 0
        if (calendar.timeInMillis > Calendar.getInstance().timeInMillis) {
            var alarmTime: Long = calendar.timeInMillis
            viewModel.addAlarm(makeAlarm(alarmTime, randomReq))
            println(dtToTime(alarmTime))
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntentA)
            Toast.makeText(context, "Alarm set", Toast.LENGTH_LONG).show()
            requireActivity().registerReceiver(AlertReceiver(), IntentFilter())

        } else {
            Toast.makeText(context, "Enter Valid Time", Toast.LENGTH_LONG).show()

        }
    }

    private fun dtToTime(untixTimeStamp: Long): String {
        val timeFormat = SimpleDateFormat("hh:mm a")
        val date = Date()
        date.time = untixTimeStamp * 1000
        return timeFormat.format(date)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myDay = day
        myYear = year
        myMonth = month
        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(
            context, this@AlertsFragment, hour, minute,
            DateFormat.is24HourFormat(context)
        )
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        myHour = hourOfDay
        myMinute = minute

    }

    fun getAlertsFromCurrent() = launch {
        viewModel.weather.await().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it == null) return@Observer
            if (it.alerts!!.isNotEmpty()) {
                ALERTDESCRIPTION = it.alerts!![0].description
                ALERTTIMESTART = it.alerts!![0].start
                ALERTTIMEEND = it.alerts!![0].end
            }
        })
    }

    var itemTouchHelper: ItemTouchHelper.SimpleCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val builder = AlertDialog.Builder(context)
                builder.setTitle("Confirm")
                builder.setMessage(getString(R.string.alarmdelete))
                builder.setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                    init()


                }
                builder.setPositiveButton("Yes") { dialog, which ->
                    var alarmid = alertAdapter.getItemByVH(viewHolder).alarmid
                    val intentA = Intent(context, AlertReceiver::class.java)
                    val pendingIntentA = PendingIntent.getBroadcast(
                        context,
                        alarmid,
                        intentA,
                        PendingIntent.FLAG_CANCEL_CURRENT
                    )
                    deleteAlarmFromDB(alertAdapter.getItemByVH(viewHolder))
                    alertAdapter.removeItem(viewHolder as AlertAdapter.ItemViewHolder)

                }
                val alert = builder.create()
                alert.show()

            }


        }

    // delete favorite item
    fun deleteAlarmFromDB(alarm: Alarms) {
        viewModel.alarmsdelete(alarm)
    }

    private fun makeAlarm(time: Long, alarmId: Int): Alarms {
        return Alarms(null, time, alarmId)
    }

}