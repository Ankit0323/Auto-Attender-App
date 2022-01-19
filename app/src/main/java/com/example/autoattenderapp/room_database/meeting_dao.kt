package com.example.autoattenderapp.room_database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface meeting_dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: meeting_data)
    @Delete
    suspend fun delete(user: meeting_data)
    @Query("Delete from meetingTable ")
    suspend fun deleteAll()
    @Query("Select * from meetingTable order by id ASC")
    fun getAllMeetings(): LiveData<List<meeting_data>>
}