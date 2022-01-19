package com.example.autoattenderapp.roomForLinks

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.joda.time.DateTime
import java.util.*

@Entity(tableName = "btmTable")
class btm_data(
                  var date:String?,
                   var className:String?,
                   var userName:String?,
                   var link: String?,
                   var startTime:String?,
                   var endTime:String?,
                  var timeDifference:String?
                   ) {
    @PrimaryKey(autoGenerate = true) var id=0
}