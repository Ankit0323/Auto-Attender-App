package com.example.autoattenderapp.roomForLinks

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class btm_view_model(application: Application): AndroidViewModel(application) {
    val allMeetings : LiveData<List<btm_data>>
    val repository: btm_repository
    init {
        val dao= btm_database.getDatabase(application).musicDao()
        repository= btm_repository(dao)
        allMeetings=repository.allMeetings
    }
    fun deleteMeeting(data : btm_data)=viewModelScope.launch(Dispatchers.IO) {
        repository.delete(data)
    }

    fun insertMeeting(data : btm_data)=viewModelScope.launch(Dispatchers.IO) {
        repository.insert(data)
    }
    fun deleteAllMeeting()=viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }
}