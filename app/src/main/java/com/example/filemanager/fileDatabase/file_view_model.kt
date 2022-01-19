package com.example.filemanager.fileDatabase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class file_view_model(application: Application): AndroidViewModel(application) {
    val allMeetings : LiveData<List<file_data>>
    val repository: file_repository
    init {
        val dao= file_database.getDatabase(application).musicDao()
        repository= file_repository(dao)
        allMeetings=repository.allMeetings
    }
    fun deleteMeeting(data : file_data)=viewModelScope.launch(Dispatchers.IO) {
        repository.delete(data)
    }

    fun insertMeeting(data : file_data)=viewModelScope.launch(Dispatchers.IO) {
        repository.insert(data)
    }
    fun deleteAllMeeting()=viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }
}