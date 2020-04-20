package com.mehul.redditwall.databases

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.mehul.redditwall.objects.FavImage
import java.util.concurrent.ExecutionException

class FavRepository constructor(application: Context) {
    private val favDAO: FavDAO
    val allFav: LiveData<List<FavImage>>

    val favAsList: List<FavImage>
        get() {
            try {
                return GetFavListAsyncTask(favDAO).execute().get()
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            return ArrayList()
        }

    suspend fun insert(saved: FavImage) {
        favDAO.insert(saved)
    }

    suspend fun deleteAll() {
        favDAO.deleteAll()
    }

    suspend fun deleteFav(saved: FavImage) {
        favDAO.deleteFavImage(saved)
    }

    private class GetFavListAsyncTask internal constructor(private val mAsyncTaskDao: FavDAO)
        : AsyncTask<Void, Void, List<FavImage>>() {
        override fun doInBackground(vararg voids: Void): List<FavImage> {
            return mAsyncTaskDao.favAsList
        }

    }

    init {
        val db = FavRoomDatabase.getDatabase(application)
        favDAO = db!!.favDAO()
        allFav = favDAO.allFavImages
    }
}