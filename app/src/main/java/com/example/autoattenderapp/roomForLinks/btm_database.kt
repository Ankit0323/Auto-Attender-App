package com.example.autoattenderapp.roomForLinks

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(btm_data::class), version = 1, exportSchema = false)
abstract class btm_database : RoomDatabase() {
    abstract fun musicDao(): btm_dao

    companion object {


        @Volatile
        private var INSTANCE: btm_database? = null

        fun getDatabase(context: Context): btm_database {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        btm_database::class.java,
                        "btmTable"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}