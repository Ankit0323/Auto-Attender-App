package com.example.filemanager.fileDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.joda.time.DateTime
import java.util.*

@Entity(tableName = "fileTable")
class file_data(
                   var filePath:String?,
                   var date: String?
                   ) {
    @PrimaryKey(autoGenerate = true) var id=0
}