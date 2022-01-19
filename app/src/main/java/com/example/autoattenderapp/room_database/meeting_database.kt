package com.example.autoattenderapp.room_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(meeting_data::class), version = 1, exportSchema = false)
abstract class meeting_database : RoomDatabase() {
    abstract fun musicDao(): meeting_dao

    companion object {


        @Volatile
        private var INSTANCE: meeting_database? = null

        fun getDatabase(context: Context): meeting_database {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        meeting_database::class.java,
                        "meetingTable"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}