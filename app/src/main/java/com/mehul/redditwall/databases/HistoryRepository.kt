package com.mehul.redditwall.databases

import android.content.Context
import androidx.lifecycle.LiveData
import com.mehul.redditwall.objects.HistoryItem

class HistoryRepository internal constructor(application: Context) {
    private val histDao: HistoryDAO
    val allHistoryItems: LiveData<List<HistoryItem>>

    suspend fun insert(history: HistoryItem) {
        histDao.insert(history)
    }

    suspend fun deleteAll() {
        histDao.deleteAll()
    }

    suspend fun deleteHist(history: HistoryItem) {
        histDao.deleteHistoryItem(history)
    }

    init {
        val db = HistRoomDatabase.getDatabase(application)
        histDao = db!!.historyDAO()
        allHistoryItems = histDao.allHistoryItems
    }
}