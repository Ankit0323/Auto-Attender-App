package com.example.autoattenderapp.roomForLinks

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface btm_dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: btm_data)
    @Delete
    suspend fun delete(user: btm_data)
    @Query("Delete from btmTable ")
    suspend fun deleteAll()
    @Query("Select * from btmTable order by id ASC")
    fun getAllMeetings(): LiveData<List<btm_data>>
}