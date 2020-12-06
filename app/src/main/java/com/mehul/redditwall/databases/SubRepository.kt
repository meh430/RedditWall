package com.mehul.redditwall.databases

import android.content.Context
import androidx.lifecycle.LiveData
import com.mehul.redditwall.objects.Subreddit

class SubRepository internal constructor(application: Context) {
    private val subDAO: SubDAO
    val allSubreddit: LiveData<List<Subreddit>>

    suspend fun insert(saved: Subreddit) {
        subDAO.insert(saved)
    }

    suspend fun deleteAll() {
        subDAO.deleteAll()
    }

    suspend fun deleteSubreddit(saved: Subreddit) {
        subDAO.deleteSubreddit(saved)
    }

    init {
        val db = SubRoomDatabase.getDatabase(application)
        subDAO = db!!.subDao()
        allSubreddit = subDAO.allSubreddit
    }
}