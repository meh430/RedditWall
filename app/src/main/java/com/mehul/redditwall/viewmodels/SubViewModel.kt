package com.mehul.redditwall.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.mehul.redditwall.databases.SubRepository
import com.mehul.redditwall.objects.SubSaved
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

public class SubViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: SubRepository = SubRepository(application.applicationContext)
    val allSubs: LiveData<List<SubSaved>>

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repo.deleteAll()
    }

    fun insert(saved: SubSaved) = viewModelScope.launch(Dispatchers.IO) {
        repo.insert(saved)
    }

    fun deleteSavedSub(saved: SubSaved) = viewModelScope.launch(Dispatchers.IO) {
        repo.deleteSubSaved(saved)
    }

    init {
        allSubs = repo.allSubSaved
    }
}