package com.mehul.redditwall.history

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData

class HistoryRepository internal constructor(application: Context) {
    private val histDao: HistoryDAO?
    val allHistoryItems: LiveData<List<HistoryItem?>?>?

    fun insert(history: HistoryItem?) {
        InsertAsyncTask(histDao).execute(history)
    }

    fun deleteAll() {
        DeleteAllHistAsyncTask(histDao).execute()
    }

    fun deleteHist(history: HistoryItem?) {
        DeleteHistAsyncTask(histDao).execute(history)
    }

    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: HistoryDAO?) : AsyncTask<HistoryItem?, Void?, Void?>() {
        override fun doInBackground(vararg params: HistoryItem?): Void? {
            mAsyncTaskDao?.insert(params[0])
            return null
        }

    }

    private class DeleteAllHistAsyncTask internal constructor(private val mAsyncTaskDao: HistoryDAO?) : AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg voids: Void?): Void? {
            mAsyncTaskDao?.deleteAll()
            return null
        }

    }

    private class DeleteHistAsyncTask internal constructor(private val mAsyncTaskDao: HistoryDAO?) : AsyncTask<HistoryItem?, Void?, Void?>() {
        override fun doInBackground(vararg params: HistoryItem?): Void? {
            mAsyncTaskDao?.deleteHistoryItem(params[0])
            return null
        }

    }

    init {
        val db = HistRoomDatabase.getDatabase(application)
        histDao = db!!.historyDAO()
        allHistoryItems = histDao!!.allHistoryItems
    }
}