package com.example.autoattenderapp


import android.app.ActionBar
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.baoyachi.stepview.VerticalStepView
import com.example.autoattenderapp.databinding.ActivityMainBinding
import com.example.autoattenderapp.roomForLinks.btm_data
import com.example.autoattenderapp.roomForLinks.btm_view_model
import com.example.autoattenderapp.room_database.meeting_data
import com.example.autoattenderapp.room_database.meeting_view_model

import com.example.autoattenderapp.todoapp.TodoActivity
import com.example.filemanager.FileActivity
import com.example.filemanager.fileListRecyclerAdapter
import com.example.todoapp.notesDate
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import org.joda.time.DateTime
import java.sql.Time
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(),DatePickerListener {
    private lateinit var binding: ActivityMainBinding
    var date: DateTime? = null
    var meetCount = 1
    var countbefore = 0
    var sorted_meetingList = ArrayList<meeting_data>()
    var dateForFile: DateTime? = null
    private lateinit var btmAdapter: linkRecyclerAdapter
    var meetingList = ArrayList<meeting_data>()
    var btmList = ArrayList<btm_data>()
var btmSortedList=ArrayList<btm_data>()
    // var sorted_meetingList =ArrayList<meeting_data>()
    private lateinit var viewModel: meeting_view_model
    private lateinit var btmViewModel: btm_view_model
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        date = DateTime.now()
        binding.scrollText.isSelected = true
        binding.notes.setOnClickListener {
            val intent = Intent(this, TodoActivity::class.java)
           notesDate.date=dateForFile.toString()
            startActivity(intent)
        }
        binding.attachment.setOnClickListener {
            val intent = Intent(this, FileActivity::class.java)
            intent.putExtra("DATE", dateForFile.toString())
            startActivity(intent)

        }
        binding.calender
            .setListener(this)
            .setDays(90)
            .setOffset(7)
            .setDateSelectedColor(ContextCompat.getColor(this, R.color.light))
            .setDateSelectedTextColor(ContextCompat.getColor(this, R.color.blue))
            .setMonthAndYearTextColor(ContextCompat.getColor(this, R.color.white1))
            .setTodayButtonTextColor(ContextCompat.getColor(this, R.color.white1))
            .setTodayDateTextColor(ContextCompat.getColor(this, R.color.white1))
            .setTodayDateBackgroundColor(ContextCompat.getColor(this, R.color.blue1))
            .setUnselectedDayTextColor(ContextCompat.getColor(this, R.color.white1))
            .setDayOfWeekTextColor(ContextCompat.getColor(this, R.color.white1))
            .setUnselectedDayTextColor(ContextCompat.getColor(this, R.color.white1))
            .showTodayButton(true)
            .init()
        binding.calender.backgroundColor = ContextCompat.getColor(this, R.color.blue1)
        binding.calender.setDate(DateTime.now())

        binding.calender.scaleX = 1f
        binding.calender.scaleY = 1.7f

/////////////////////////////////////
        // view model
        viewModel = ViewModelProvider(
            this, ViewModelProvider
                .AndroidViewModelFactory.getInstance(application)
        ).get(meeting_view_model::class.java)
        viewModel.allMeetings.observe(this, androidx.lifecycle.Observer {
            meetingList.clear()
            meetingList.addAll(it)
        })

        ///////////////////////////////////////////////
        // btm view model
        btmViewModel = ViewModelProvider(
            this, ViewModelProvider
                .AndroidViewModelFactory.getInstance(application)
        ).get(btm_view_model::class.java)
        btmViewModel.allMeetings.observe(this, androidx.lifecycle.Observer {
            btmList.addAll(it)
        })
        ///////////////////////////////////////////////////
        // recycler view btm_sheet
        var layoutManager = LinearLayoutManager(this)
        binding.recycleBtmSheet.layoutManager = layoutManager
        btmAdapter = linkRecyclerAdapter(this)
        binding.recycleBtmSheet.adapter = btmAdapter


           val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDateandTime: String = sdf.format(Date())
        val formatter2 = SimpleDateFormat("yyyy-MM-dd")
        val date3: Date = formatter2.parse(currentDateandTime)



        ////////////////////////////////
// for btm List
        for(data in btmList) {
            val formatter2 = SimpleDateFormat("yyyy-MM-dd")
            val date2: Date = formatter2.parse(data.date)

            if (date3.equals(date2)) {
                btmSortedList.add(data)
            }
        }
        btmAdapter.updateAll(btmSortedList)

//binding.btmSheet.layo

//viewModel.deleteAllMeeting()
        /////////////////////////////////////
        // vertical progress bar




//        0
        val list0: MutableList<String> = ArrayList()
        list0.add("")
        binding.stepView0.setStepsViewIndicatorComplectingPosition(list0.size - 2)
            .reverseDraw(false) //default is true
            .setStepViewTexts(list0)
            .setLinePaddingProportion(2.2f)
        ////////////////////////////////////////////////////////////////////////////////////
//         1
        val list1: MutableList<String> = ArrayList()
        list1.add("")
        list1.add("")
        binding.stepView1.setStepsViewIndicatorComplectingPosition(list1.size - 2)
            .reverseDraw(false) //default is true
            .setStepViewTexts(list1)
            .setLinePaddingProportion(2.2f)
        ////////////////////////////////////////////////////////////////////////////////////
//         2
        val list2: MutableList<String> = ArrayList()
        list2.add("")
        list2.add("")
        list2.add("")
        binding.stepView2.setStepsViewIndicatorComplectingPosition(list2.size - 2)
            .reverseDraw(false) //default is true
            .setStepViewTexts(list2)
            .setLinePaddingProportion(2.2f)
        ////////////////////////////////////////////////////////////////////////////////////
//         3
        val list3: MutableList<String> = ArrayList()
        list3.add("")
        list3.add("")
        list3.add("")
        list3.add("")
        binding.stepView3.setStepsViewIndicatorComplectingPosition(list3.size - 2)
            .reverseDraw(false) //default is true
            .setStepViewTexts(list3)
            .setLinePaddingProportion(2.2f)
        ////////////////////////////////////////////////////////////////////////////////////
//         4
        val list4: MutableList<String> = ArrayList()
        list4.add("")
        list4.add("")
        list4.add("")
        list4.add("")
        list4.add("")
        binding.stepView4.setStepsViewIndicatorComplectingPosition(list4.size - 2)
            .reverseDraw(false) //default is true
            .setStepViewTexts(list4)
            .setLinePaddingProportion(2.2f)

    }


    private fun startTimePicker(myDialog: Dialog) {
        val is24Hour = is24HourFormat(applicationContext)
        var cal: Calendar = Calendar.getInstance()
        var time: String? = null
        val clockFormat = if (is24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setHour(12)
            .setMinute(0)
            .setTitleText("set Time")
            .build()
        picker.show(supportFragmentManager, "TAG")
        picker.addOnPositiveButtonClickListener {

            cal.set(Calendar.HOUR_OF_DAY, picker.hour)
            cal.set(Calendar.MINUTE, picker.minute)
            cal.set(Calendar.SECOND, 0)
            //    var hour : Int?=picker.hour
            //   var minute:Int?=picker.minute
            //  var ampm:String?=null

            time = DateFormat.getTimeInstance(DateFormat.SHORT).format(cal.time)
            myDialog.findViewById<TextView>(R.id.startTime).setText(time)


        }

    }

    private fun endTimePicker(myDialog: Dialog) {
        val is24Hour = is24HourFormat(applicationContext)
        var cal: Calendar = Calendar.getInstance()
        var time: String? = null
        val clockFormat = if (is24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setHour(12)
            .setMinute(0)
            .setTitleText("set Time")
            .build()
        picker.show(supportFragmentManager, "TAG")
        picker.addOnPositiveButtonClickListener {

            cal.set(Calendar.HOUR_OF_DAY, picker.hour)
            cal.set(Calendar.MINUTE, picker.minute)
            cal.set(Calendar.SECOND, 0)
            //    var hour : Int?=picker.hour
            //   var minute:Int?=picker.minute
            //  var ampm:String?=null
            time = DateFormat.getTimeInstance(DateFormat.SHORT).format(cal.time)
            myDialog.findViewById<TextView>(R.id.endTime).setText(time)


        }
        // return time
    }

    fun verticalStepView(steps: Int) {
        when (steps) {
            0 -> {
                binding.stepView0.visibility = View.VISIBLE
                binding.stepView1.visibility = View.GONE
                binding.stepView2.visibility = View.GONE
                binding.stepView3.visibility = View.GONE
                binding.stepView4.visibility = View.GONE
            }
            1 -> {
                binding.stepView0.visibility = View.GONE
                binding.stepView1.visibility = View.VISIBLE
                binding.stepView2.visibility = View.GONE
                binding.stepView3.visibility = View.GONE
                binding.stepView4.visibility = View.GONE
            }
            2 -> {
                binding.stepView0.visibility = View.GONE
                binding.stepView1.visibility = View.GONE
                binding.stepView2.visibility = View.VISIBLE
                binding.stepView3.visibility = View.GONE
                binding.stepView4.visibility = View.GONE
            }
            3 -> {
                binding.stepView0.visibility = View.GONE
                binding.stepView1.visibility = View.GONE
                binding.stepView2.visibility = View.GONE
                binding.stepView3.visibility = View.VISIBLE
                binding.stepView4.visibility = View.GONE
            }
            4 -> {
                binding.stepView0.visibility = View.GONE
                binding.stepView1.visibility = View.GONE
                binding.stepView2.visibility = View.GONE
                binding.stepView3.visibility = View.GONE
                binding.stepView4.visibility = View.VISIBLE
            }
        }

    }


    override fun onDateSelected(dateSelected: DateTime?) {
        dateForFile = dateSelected
        binding.scrollText.setText("             No meeting is scheduled right now                 ")
      sorted_meetingList.clear()
        btmSortedList.clear()
        countbefore = 0
        before(1)
        before(2)
        before(3)
        //Its for current date
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDateandTime: String = sdf.format(Date())
        val formatter2 = SimpleDateFormat("yyyy-MM-dd")
        val date3: Date = formatter2.parse(currentDateandTime)


        var myDate: String = dateSelected.toString()
        val formatter1 = SimpleDateFormat("yyyy-MM-dd")
        val date1: Date = formatter1.parse(myDate)
        ////////////////////////////////
// for btm List
        for(data in btmList) {
            val formatter2 = SimpleDateFormat("yyyy-MM-dd")
            val date2: Date = formatter2.parse(data.date)

            if (date1.equals(date2)) {
                btmSortedList.add(data)
            }
        }
        btmAdapter.updateAll(btmSortedList)




        binding.actionBtn.setOnClickListener {
            btmDialog(dateSelected)
        }








        for (data in meetingList) {


            val formatter2 = SimpleDateFormat("yyyy-MM-dd")
            val date2: Date = formatter2.parse(data.date)



            if (date1.equals(date2)) {

                countbefore++
                sorted_meetingList.add(data)
            }


        }

        putData(sorted_meetingList, countbefore, dateSelected, date1, date3)
        /* if(date!!.compareTo(dateSelected)>0){
        binding.calender.setDateSelectedColor(ContextCompat.getColor(this, R.color.red))
 }*/

        //   val date :String=Calendar.getInstance().toString()


// not completed

        binding.card1.setOnClickListener {
            if (date1.compareTo(date3) < 0) {
                Toast.makeText(
                    this,
                    "Can't schedule your meeting",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (countbefore == 0) {

                    myDialogSelected(dateSelected, 1)

                } else {
                    Toast.makeText(
                        this,
                        "Meeting already scheduled",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
        binding.card2.setOnClickListener {
            if (date1.compareTo(date3) < 0) {
                Toast.makeText(
                    this,
                    "Can't schedule your meeting",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (countbefore < 1) {
                    Toast.makeText(
                        this,
                        "Please schedule the above meeting first",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (countbefore == 1) {

                    myDialogSelected(dateSelected, 2)
                } else {
                    Toast.makeText(
                        this,
                        "Meeting already scheduled",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
        binding.card3.setOnClickListener {
            if (date1.compareTo(date3) < 0) {
                Toast.makeText(
                    this,
                    "Can't schedule your meeting",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (countbefore < 2) {
                    Toast.makeText(
                        this,
                        "Please schedule the above meeting first",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (countbefore == 2) {
                    myDialogSelected(dateSelected, 3)
                } else {
                    Toast.makeText(
                        this,
                        "Sorry,you can't schedule more meetings",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }


    }

    fun btmDialog(dateSelected: DateTime?) {
        var myDialog = Dialog(this)
        myDialog.setContentView(R.layout.dialog_btm)
        val window: Window? = myDialog.window
        window!!.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.WRAP_CONTENT
        )
        myDialog.show()
        val formatdate =
            DateFormat.getDateInstance(DateFormat.LONG).format(dateSelected!!.toDate())

        myDialog.findViewById<TextView>(R.id.date).setText(formatdate)
        myDialog.findViewById<TextView>(R.id.startTime).setOnClickListener {
            startTimePicker(myDialog)
        }
        myDialog.findViewById<TextView>(R.id.endTime).setOnClickListener {
            endTimePicker(myDialog)
        }

        myDialog.findViewById<Button>(R.id.save).setOnClickListener {
            if (myDialog.findViewById<TextView>(R.id.className).text.isNullOrEmpty()) {
                Toast.makeText(this, "Meeting name is required", Toast.LENGTH_SHORT).show()
            } else if (myDialog.findViewById<TextView>(R.id.yourName).text.isNullOrEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            } else if (myDialog.findViewById<TextView>(R.id.link).text.isNullOrEmpty()) {
                Toast.makeText(this, "Paste the meeting link", Toast.LENGTH_SHORT).show()
            } else if (myDialog.findViewById<TextView>(R.id.startTime).text.isNullOrEmpty()) {
                Toast.makeText(this, "Please fill the start time", Toast.LENGTH_SHORT).show()
            } else if (myDialog.findViewById<TextView>(R.id.endTime).text.isNullOrEmpty()) {
                Toast.makeText(this, "Please fill the end time", Toast.LENGTH_SHORT).show()
            } else {
                var startTime = myDialog.findViewById<TextView>(R.id.startTime).text.toString()
                var endTime = myDialog.findViewById<TextView>(R.id.endTime).text.toString()

                var f1: Int = startTime!!.substring(0, 2).toInt()
                var e1: Int = endTime!!.substring(0, 2).toInt()
                var f2: Int = startTime.substring(3, startTime.length).toInt()
                var e2: Int = startTime.substring(3, endTime.length).toInt()
                var hourtimeDiff: Int = e1 - f1
                var mintimediff: Int = e2 - f2

                if (f1 > e1 || (f1 == e1 && f2 >= e2)) {
                    Toast.makeText(
                        this,
                        "End time should be greater than start time",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (mintimediff < 0) {
                        mintimediff = 60 + mintimediff
                        hourtimeDiff--
                    }
                    var timeDifference = ""
                    if (hourtimeDiff == 1 && mintimediff == 0) {
                        timeDifference = "${hourtimeDiff}:${mintimediff}0 hr"
                    } else {
                        timeDifference = "${hourtimeDiff}:${mintimediff}0 hrs"
                    }
                    /////////////////

                    var meetingName =
                        myDialog.findViewById<TextView>(R.id.className).text.toString()
                    var yourName = myDialog.findViewById<TextView>(R.id.yourName).text.toString()
                    var link = myDialog.findViewById<TextView>(R.id.link).text.toString()


                    var date = dateSelected.toString()


                    btmViewModel.insertMeeting(
                        btm_data(
                            date,
                            meetingName,
                            yourName,
                            link,
                            startTime,
                            endTime,
                            timeDifference
                        )
                    )
                    btmSortedList.add(btm_data(
                        date,
                        meetingName,
                        yourName,
                        link,
                        startTime,
                        endTime,
                        timeDifference
                    ))
btmAdapter.updateAll(btmSortedList)

                }
                myDialog.dismiss()
            }
        }

        }
        fun myDialogSelected(dateSelected: DateTime?, meetingNumber: Int) {

            var resource = resources.getStringArray(R.array.priority)
            var arrayAdapter = ArrayAdapter(this, R.layout.drop_down, resource)
            var myDialog = Dialog(this)
            var priority: String? = "High"

            myDialog.setContentView(R.layout.dialog)

            val formatdate =
                DateFormat.getDateInstance(DateFormat.MEDIUM).format(dateSelected!!.toDate())

            //   myDialog.findViewById<TextView>(R.id.date).setText(dateSelected.toString().substring(0, 10))
            myDialog.findViewById<TextView>(R.id.date).setText(formatdate)

            // for priority
            myDialog.findViewById<AutoCompleteTextView>(R.id.prText).setAdapter(arrayAdapter)
            myDialog.findViewById<AutoCompleteTextView>(R.id.prText)
                .setOnItemClickListener { adapterView, view, i, l ->
                    var text: String? = adapterView.getItemAtPosition(i).toString()
                    priority = text
                    Log.i("priority", priority.toString())
                    if (text.equals("High")) {

                        myDialog.findViewById<ImageView>(R.id.priorityColor)
                            .setImageResource(R.color.red)
                    } else if (text.equals("Medium")) {

                        myDialog.findViewById<ImageView>(R.id.priorityColor)
                            .setImageResource(R.color.yellow)
                    } else if (text.equals("Low")) {
                        myDialog.findViewById<ImageView>(R.id.priorityColor)
                            .setImageResource(R.color.green)
                    } else {


                        myDialog.findViewById<ImageView>(R.id.priorityColor)
                            .setImageResource(R.color.red)

                    }
                }


            myDialog.show()

            // for start or end time

            val window: Window? = myDialog.window
            window!!.setLayout(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT
            )
            myDialog.findViewById<TextView>(R.id.startTime).setOnClickListener {
                startTimePicker(myDialog)
            }
            myDialog.findViewById<TextView>(R.id.endTime).setOnClickListener {
                endTimePicker(myDialog)
            }

            /////////////////////
            // saving all details

            myDialog.findViewById<Button>(R.id.save).setOnClickListener {
                if (myDialog.findViewById<TextView>(R.id.className).text.isNullOrEmpty()) {
                    Toast.makeText(this, "Meeting name is required", Toast.LENGTH_SHORT).show()
                } else if (myDialog.findViewById<TextView>(R.id.yourName).text.isNullOrEmpty()) {
                    Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
                } else if (myDialog.findViewById<TextView>(R.id.link).text.isNullOrEmpty()) {
                    Toast.makeText(this, "Paste the meeting link", Toast.LENGTH_SHORT).show()
                } else if (myDialog.findViewById<TextView>(R.id.email).text.isNullOrEmpty()) {
                    Toast.makeText(this, "Email required", Toast.LENGTH_SHORT).show()
                } else if (myDialog.findViewById<TextView>(R.id.startTime).text.isNullOrEmpty()) {
                    Toast.makeText(this, "Please fill the start time", Toast.LENGTH_SHORT).show()
                } else if (myDialog.findViewById<TextView>(R.id.endTime).text.isNullOrEmpty()) {
                    Toast.makeText(this, "Please fill the end time", Toast.LENGTH_SHORT).show()
                } else {
                    var startTime = myDialog.findViewById<TextView>(R.id.startTime).text.toString()
                    var endTime = myDialog.findViewById<TextView>(R.id.endTime).text.toString()

                    var f1: Int = startTime!!.substring(0, 2).toInt()
                    var e1: Int = endTime!!.substring(0, 2).toInt()
                    var f2: Int = startTime.substring(3, startTime.length).toInt()
                    var e2: Int = startTime.substring(3, endTime.length).toInt()
                    var hourtimeDiff: Int = e1 - f1
                    var mintimediff: Int = e2 - f2

                    if (f1 > e1 || (f1 == e1 && f2 >= e2)) {
                        Toast.makeText(
                            this,
                            "End time should be greater than start time",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        if (mintimediff < 0) {
                            mintimediff = 60 + mintimediff
                            hourtimeDiff--
                        }
                        var timeDifference = ""
                        if (hourtimeDiff == 1 && mintimediff == 0) {
                            timeDifference = "${hourtimeDiff}:${mintimediff}0 hr"
                        } else {
                            timeDifference = "${hourtimeDiff}:${mintimediff}0 hrs"
                        }
                        /////////////////
                        var number = meetingNumber
                        var meetingName =
                            myDialog.findViewById<TextView>(R.id.className).text.toString()
                        var yourName =
                            myDialog.findViewById<TextView>(R.id.yourName).text.toString()
                        var link = myDialog.findViewById<TextView>(R.id.link).text.toString()
                        var email = myDialog.findViewById<TextView>(R.id.email).text.toString()

                        var date = dateSelected.toString()


                        viewModel.insertMeeting(
                            meeting_data(
                                number,
                                meetingName,
                                yourName,
                                link,
                                email,
                                startTime,
                                endTime,
                                date,
                                priority,
                                timeDifference
                            )
                        )
                        Log.i("priority1", priority.toString())
                        countbefore++
                        if (meetingNumber == 1) {
                            verticalStepView(1)
                            binding.startTime1.visibility = View.VISIBLE
                            binding.clock1.visibility = View.GONE
                            binding.startTime1.setText(startTime)
                            binding.user1.setText(yourName)
                            binding.schedule1.setText("Class Scheduled")

                            binding.name1.setText(meetingName)
                            binding.duration1.setText(timeDifference)


                            if (priority.equals("High")) {
                                binding.priorityColor1.setImageResource(R.color.red)
                            } else if (priority.equals("Medium")) {
                                binding.priorityColor1.setImageResource(R.color.yellow)
                            } else if (priority.equals("Low")) {
                                binding.priorityColor1.setImageResource(R.color.green)
                            } else {
                                binding.priorityColor1.setImageResource(R.color.red)
                            }
                        } else if (meetingNumber == 2) {
                            binding.startTime2.visibility = View.VISIBLE
                            binding.clock2.visibility = View.GONE
                            binding.startTime2.setText(startTime)
                            binding.user2.setText(yourName)
                            binding.schedule2.setText("Class Scheduled")

                            binding.name2.setText(meetingName)
                            binding.duration2.setText(timeDifference)


                            if (priority.equals("High")) {
                                binding.priorityColor2.setImageResource(R.color.red)
                            } else if (priority.equals("Medium")) {
                                binding.priorityColor2.setImageResource(R.color.yellow)
                            } else if (priority.equals("Low")) {
                                binding.priorityColor2.setImageResource(R.color.green)
                            } else {
                                binding.priorityColor2.setImageResource(R.color.yellow)
                            }
                        } else if (meetingNumber == 3) {
                            binding.startTime3.visibility = View.VISIBLE
                            binding.clock3.visibility = View.GONE
                            binding.startTime3.setText(startTime)
                            binding.user3.setText(yourName)
                            binding.schedule3.setText("Class Scheduled")

                            binding.name3.setText(meetingName)
                            binding.duration3.setText(timeDifference)


                            if (priority.equals("High")) {
                                binding.priorityColor3.setImageResource(R.color.red)
                            } else if (priority.equals("Medium")) {
                                binding.priorityColor3.setImageResource(R.color.yellow)
                            } else if (priority.equals("Low")) {
                                binding.priorityColor3.setImageResource(R.color.green)
                            } else {
                                binding.priorityColor3.setImageResource(R.color.green)
                            }
                        }


                    }
                }
                myDialog.dismiss()
            }


        }


        fun reverseDraw(isReverse: Boolean): VerticalStepView? {
            return binding.stepView0.reverseDraw(isReverse)
        }

        fun putData(
            sorted_meetingList: ArrayList<meeting_data>,
            countbefore: Int,
            dateSelected: DateTime?,
            date1: Date,
            date3: Date
        ) {
            val formatdate =
                DateFormat.getDateInstance(DateFormat.MEDIUM).format(dateSelected!!.toDate())

            if (countbefore == 0) {
                verticalStepView(0)
            }
            if (countbefore > 0) {
                binding.scrollText.setText("            1 Meeting is scheduled on ${formatdate}                ")
                verticalStepView(1)
                var data = sorted_meetingList[0]
                binding.startTime1.visibility = View.VISIBLE
                binding.clock1.visibility = View.GONE
                binding.startTime1.setText(data.startTime)
                binding.user1.setText(data.yourName)
                binding.name1.setText(data.meetingName)
                binding.duration1.setText(data.timeDiff)

                if (date3.compareTo(date1) < 0) {
                    binding.schedule1.setText("Class Scheduled")
                } else if (date3.compareTo(date1) > 0) {
                    verticalStepView(2)
                    binding.schedule1.setText("Class Completed")
                } else if (date3.compareTo(date1) == 0) {
                    val simpleDateFormat = SimpleDateFormat("hh:mm")
                    var cal = Calendar.getInstance().time
                    var time1 = simpleDateFormat.parse(data.endTime)
                    var t = DateFormat.getTimeInstance(DateFormat.SHORT).format(cal)
                    var currentTime = simpleDateFormat.parse(t)
                    if (currentTime.compareTo(time1) >= 0) {
                        verticalStepView(2)
                        binding.schedule1.setText("Class Completed")
                    } else {
                        binding.schedule1.setText("Class Scheduled")
                    }
                }
            }

            if (countbefore > 1) {
                binding.scrollText.setText("            2 Meetings are scheduled on ${formatdate}                ")

                var data = sorted_meetingList[1]
                binding.startTime2.visibility = View.VISIBLE
                binding.clock2.visibility = View.GONE
                binding.startTime2.setText(data.startTime)
                binding.user2.setText(data.yourName)


                binding.name2.setText(data.meetingName)
                binding.duration2.setText(data.timeDiff)


                if (date3.compareTo(date1) < 0) {
                    binding.schedule2.setText("Class Scheduled")
                } else if (date3.compareTo(date1) > 0) {
                    verticalStepView(3)
                    binding.schedule2.setText("Class Completed")
                } else if (date3.compareTo(date1) == 0) {
                    val simpleDateFormat = SimpleDateFormat("hh:mm")
                    var cal = Calendar.getInstance().time
                    var time1 = simpleDateFormat.parse(data.endTime)
                    var t = DateFormat.getTimeInstance(DateFormat.SHORT).format(cal)
                    var currentTime = simpleDateFormat.parse(t)

                    if (currentTime.compareTo(time1) >= 0) {
                        verticalStepView(3)
                        binding.schedule2.setText("Class Completed")
                    } else {
                        binding.schedule2.setText("Class Scheduled")
                    }

                }
            }
            if (countbefore > 2) {
                binding.scrollText.setText("            3 Meetings are scheduled on ${formatdate}                ")

                var data = sorted_meetingList[2]
                binding.startTime3.visibility = View.VISIBLE
                binding.clock3.visibility = View.GONE
                binding.startTime3.setText(data.startTime)
                //binding.fakeClock3.visibility=View.GONE
                binding.user3.setText(data.yourName)


                binding.name3.setText(data.meetingName)
                binding.duration3.setText(data.timeDiff)

                if (date3.compareTo(date1) < 0) {
                    binding.schedule3.setText("Class Scheduled")
                } else if (date3.compareTo(date1) > 0) {
                    verticalStepView(4)
                    binding.schedule3.setText("Class Completed")
                } else if (date3.compareTo(date1) == 0) {
                    val simpleDateFormat = SimpleDateFormat("hh:mm")
                    var cal = Calendar.getInstance().time
                    var time1 = simpleDateFormat.parse(data.endTime)
                    var t = DateFormat.getTimeInstance(DateFormat.SHORT).format(cal)
                    var currentTime = simpleDateFormat.parse(t)
                    if (currentTime.compareTo(time1) >= 0) {
                        verticalStepView(4)
                        binding.schedule3.setText("Class Completed")
                    } else {
                        binding.schedule3.setText("Class Scheduled")
                    }
                }
            }

            ////////////////////////////////////////////


        }

        fun before(count: Int) {
            if (count == 1) {
                binding.clock1.visibility = View.VISIBLE
                binding.fakeClock1.visibility = View.GONE
                binding.startTime1.visibility = View.GONE
                binding.user1.setText("User Name")
                binding.schedule1.setText("Not scheduled")

                binding.name1.setText("Class Name")
                binding.duration1.setText("Duration : '??'")

            }
            if (count == 2) {
                binding.clock2.visibility = View.VISIBLE
                binding.fakeClock2.visibility = View.GONE
                binding.startTime2.visibility = View.GONE
                binding.user2.setText("User Name")
                binding.schedule2.setText("Not scheduled")

                binding.name2.setText("Class Name")
                binding.duration2.setText("Duration : '??'")

            }
            if (count == 3) {
                binding.clock3.visibility = View.VISIBLE
                binding.fakeClock3.visibility = View.GONE
                binding.startTime3.visibility = View.GONE
                binding.user3.setText("User Name")
                binding.schedule3.setText("Not scheduled")

                binding.name3.setText("Class Name")
                binding.duration3.setText("Duration : '??'")

            }



    }
}