package com.mehul.redditwall.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.mehul.redditwall.databases.FavRepository
import com.mehul.redditwall.objects.FavImage

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