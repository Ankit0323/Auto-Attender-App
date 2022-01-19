package com.example.filemanager.fileDatabase

import androidx.lifecycle.LiveData

class file_repository(private val mDao:file_dao) {
    val allMeetings: LiveData<List<file_data>> = mDao.getAllMeetings()
    suspend fun insert(data: file_data){
        mDao.insert(data)
    }

    suspend fun delete(data: file_data){
        mDao.delete(data)
    }
    suspend fun deleteAll(){
        mDao.deleteAll()
    }
}