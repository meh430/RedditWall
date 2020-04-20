package com.mehul.redditwall.databases

import android.content.Context
import androidx.lifecycle.LiveData
import com.mehul.redditwall.objects.SubSaved

class SubRepository internal constructor(application: Context) {
    private val subDAO: SubDAO
    val allSubSaved: LiveData<List<SubSaved>>

    suspend fun insert(saved: SubSaved) {
        subDAO.insert(saved)
    }

    suspend fun deleteAll() {
        subDAO.deleteAll()
    }

    suspend fun deleteSubSaved(saved: SubSaved) {
        subDAO.deleteSubSaved(saved)
    }

    init {
        val db = SubRoomDatabase.getDatabase(application)
        subDAO = db!!.subDao()
        allSubSaved = subDAO.allSubSaved
    }
}