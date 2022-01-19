package com.example.filemanager.fileDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(file_data::class), version = 1, exportSchema = false)
abstract class file_database : RoomDatabase() {
    abstract fun musicDao(): file_dao

    companion object {


        @Volatile
        private var INSTANCE: file_database? = null

        fun getDatabase(context: Context): file_database {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        file_database::class.java,
                        "fileTable"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}