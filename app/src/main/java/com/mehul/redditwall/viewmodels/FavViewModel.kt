package com.mehul.redditwall.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.mehul.redditwall.databases.FavRepository
import com.mehul.redditwall.objects.FavImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: FavRepository = FavRepository(application.applicationContext)
    val allFav: LiveData<List<FavImage>>

    val favList: List<FavImage>
        get() = repo.favAsList

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repo.deleteAll()
    }

    fun insert(saved: FavImage) = viewModelScope.launch(Dispatchers.IO) {
        repo.insert(saved)
    }

    fun deleteFavImage(saved: FavImage) = viewModelScope.launch(Dispatchers.IO) {
        repo.deleteFav(saved)
    }

    init {
        allFav = repo.allFav
    }
}