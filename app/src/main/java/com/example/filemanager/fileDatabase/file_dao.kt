package com.example.filemanager.fileDatabase

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface file_dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: file_data)
    @Delete
    suspend fun delete(user: file_data)
    @Query("Delete from fileTable ")
    suspend fun deleteAll()
    @Query("Select * from fileTable order by id ASC")
    fun getAllMeetings(): LiveData<List<file_data>>
}