package com.mehul.redditwall.databases

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.mehul.redditwall.objects.SubSaved

class SubRepository internal constructor(application: Context) {
    private val subDAO: SubDAO?
    val allSubSaved: LiveData<List<SubSaved?>?>?

    fun insert(saved: SubSaved?) {
        InsertAsyncTask(subDAO).execute(saved)
    }

    fun deleteAll() {
        DeleteAllSubsAsyncTask(subDAO).execute()
    }

    fun deleteSubSaved(saved: SubSaved?) {
        DeleteSubAsyncTask(subDAO).execute(saved)
    }

    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: SubDAO?) : AsyncTask<SubSaved?, Void?, Void?>() {
        override fun doInBackground(vararg params: SubSaved?): Void? {
            mAsyncTaskDao?.insert(params[0])
            return null
        }

    }

    private class DeleteAllSubsAsyncTask internal constructor(private val mAsyncTaskDao: SubDAO?) : AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg voids: Void?): Void? {
            mAsyncTaskDao?.deleteAll()
            return null
        }

    }

    private class DeleteSubAsyncTask internal constructor(private val mAsyncTaskDao: SubDAO?) : AsyncTask<SubSaved?, Void?, Void?>() {
        override fun doInBackground(vararg params: SubSaved?): Void? {
            mAsyncTaskDao?.deleteSubSaved(params[0])
            return null
        }

    }

    init {
        val db = SubRoomDatabase.getDatabase(application)
        subDAO = db!!.subDao()
        allSubSaved = subDAO!!.allSubSaved
    }
}