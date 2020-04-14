package com.mehul.redditwall.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.mehul.redditwall.databases.SubRepository
import com.mehul.redditwall.objects.SubSaved

public class SubViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: SubRepository = SubRepository(application.applicationContext)
    val allSubs: LiveData<List<SubSaved?>?>?

    fun deleteAll() {
        repo.deleteAll()
    }

    fun insert(saved: SubSaved?) {
        repo.insert(saved)
    }

    fun deleteSavedSub(saved: SubSaved?) {
        repo.deleteSubSaved(saved)
    }

    init {
        allSubs = repo.allSubSaved
    }
}