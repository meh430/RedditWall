package com.mehul.redditwall.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.mehul.redditwall.databases.HistoryRepository
import com.mehul.redditwall.objects.HistoryItem

public class HistViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: HistoryRepository = HistoryRepository(application.applicationContext)
    val allHist: LiveData<List<HistoryItem?>?>?

    fun deleteAll() {
        repo.deleteAll()
    }

    fun insert(history: HistoryItem?) {
        repo.insert(history)
    }

    fun deleteHist(history: HistoryItem?) {
        repo.deleteHist(history)
    }

    init {
        allHist = repo.allHistoryItems
    }
}