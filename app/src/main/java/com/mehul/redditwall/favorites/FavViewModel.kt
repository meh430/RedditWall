package com.mehul.redditwall.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

public class FavViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: FavRepository = FavRepository(application.applicationContext)
    val allFav: LiveData<List<FavImage?>?>?

    val favList: List<FavImage?>?
        get() = repo.favAsList

    fun deleteAll() {
        repo.deleteAll()
    }

    fun insert(saved: FavImage?) {
        repo.insert(saved)
    }

    fun deleteFavImage(saved: FavImage?) {
        repo.deleteFav(saved)
    }

    init {
        allFav = repo.allFav
    }
}