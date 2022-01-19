package com.example.autoattenderapp.room_database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class meeting_view_model(application: Application): AndroidViewModel(application) {
    val allMeetings : LiveData<List<meeting_data>>
    val repository: meeting_repository
    init {
        val dao= meeting_database.getDatabase(application).musicDao()
        repository= meeting_repository(dao)
        allMeetings=repository.allMeetings
    }
    fun deleteMeeting(data : meeting_data)=viewModelScope.launch(Dispatchers.IO) {
        repository.delete(data)
    }

    fun insertMeeting(data : meeting_data)=viewModelScope.launch(Dispatchers.IO) {
        repository.insert(data)
    }
    fun deleteAllMeeting()=viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }
}