package com.example.autoattenderapp.room_database

import androidx.lifecycle.LiveData

class meeting_repository(private val mDao:meeting_dao) {
    val allMeetings: LiveData<List<meeting_data>> = mDao.getAllMeetings()
    suspend fun insert(data: meeting_data){
        mDao.insert(data)
    }

    suspend fun delete(data: meeting_data){
        mDao.delete(data)
    }
    suspend fun deleteAll(){
        mDao.deleteAll()
    }
}