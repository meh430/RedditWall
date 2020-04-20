package com.mehul.redditwall.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.mehul.redditwall.databases.HistoryRepository
import com.mehul.redditwall.objects.HistoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

public class HistViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: HistoryRepository = HistoryRepository(application.applicationContext)
    val allHist: LiveData<List<HistoryItem>>

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repo.deleteAll()
    }

    fun insert(history: HistoryItem) = viewModelScope.launch(Dispatchers.IO) {
        repo.insert(history)
    }

    fun deleteHist(history: HistoryItem) = viewModelScope.launch(Dispatchers.IO) {
        repo.deleteHist(history)
    }

    init {
        allHist = repo.allHistoryItems
    }
}