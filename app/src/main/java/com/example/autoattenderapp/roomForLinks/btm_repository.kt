package com.example.autoattenderapp.roomForLinks

import androidx.lifecycle.LiveData

class btm_repository(private val mDao:btm_dao) {
    val allMeetings: LiveData<List<btm_data>> = mDao.getAllMeetings()
    suspend fun insert(data: btm_data){
        mDao.insert(data)
    }

    suspend fun delete(data: btm_data){
        mDao.delete(data)
    }
    suspend fun deleteAll(){
        mDao.deleteAll()
    }
}