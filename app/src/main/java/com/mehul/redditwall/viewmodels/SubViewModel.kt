package com.mehul.redditwall.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.mehul.redditwall.databases.SubRepository
import com.mehul.redditwall.objects.Subreddit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

public class SubViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: SubRepository = SubRepository(application.applicationContext)
    val allSubs: LiveData<List<Subreddit>>

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repo.deleteAll()
    }

    fun insert(saved: Subreddit) = viewModelScope.launch(Dispatchers.IO) {
        repo.insert(saved)
    }

    fun deleteSubreddit(saved: Subreddit) = viewModelScope.launch(Dispatchers.IO) {
        repo.deleteSubreddit(saved)
    }

    init {
        allSubs = repo.allSubreddit
    }
}