package com.example.autoattenderapp.room_database

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.joda.time.DateTime
import java.util.*

@Entity(tableName = "meetingTable")
class meeting_data(
                   var meetingNumber:Int?,
                   var meetingName:String?,
                   var yourName:String?,
                   var link:String?,
                   var email:String?,
                   var startTime:String?,
                   var endTime:String?,
                   var date: String?,
                   var priority:String?,
                   var timeDiff:String?
                   ) {
    @PrimaryKey(autoGenerate = true) var id=0
}