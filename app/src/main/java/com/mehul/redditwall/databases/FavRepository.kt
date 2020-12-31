package com.mehul.redditwall.databases

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.mehul.redditwall.objects.WallImage
import java.util.concurrent.ExecutionException

class FavRepository constructor(application: Context) {
    private val favDAO: FavDAO
    val allFav: LiveData<List<WallImage>>

    val favAsList: List<WallImage>
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

    suspend fun insert(saved: WallImage) {
        favDAO.insert(saved)
    }

    suspend fun deleteAll() {
        favDAO.deleteAll()
    }

    suspend fun deleteFav(saved: WallImage) {
        favDAO.deleteFavImage(saved)
    }

    private class GetFavListAsyncTask internal constructor(private val mAsyncTaskDao: FavDAO)
        : AsyncTask<Void, Void, List<WallImage>>() {
        override fun doInBackground(vararg voids: Void): List<WallImage> {
            return mAsyncTaskDao.favAsList
        }

    }

    init {
        val db = FavRoomDatabase.getDatabase(application)
        favDAO = db!!.favDAO()
        allFav = favDAO.allFavImages
    }
}