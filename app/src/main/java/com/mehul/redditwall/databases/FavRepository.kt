package com.mehul.redditwall.databases

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.mehul.redditwall.objects.FavImage
import java.util.concurrent.ExecutionException

class FavRepository constructor(application: Context) {
    private val favDAO: FavDAO?
    val allFav: LiveData<List<FavImage?>?>?

    val favAsList: List<FavImage?>?
        get() {
            try {
                return GetFavListAsyncTask(favDAO).execute().get()
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            return null
        }

    fun insert(saved: FavImage?) {
        InsertAsyncTask(favDAO).execute(saved)
    }

    fun deleteAll() {
        DeleteAllFavAsyncTask(favDAO).execute()
    }

    fun deleteFav(saved: FavImage?) {
        DeleteFavAsyncTask(favDAO).execute(saved)
    }

    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: FavDAO?)
        : AsyncTask<FavImage?, Void?, Void?>() {
        override fun doInBackground(vararg params: FavImage?): Void? {
            mAsyncTaskDao?.insert(params[0])
            return null
        }
    }

    private class DeleteAllFavAsyncTask internal constructor(private val mAsyncTaskDao: FavDAO?)
        : AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg voids: Void?): Void? {
            mAsyncTaskDao?.deleteAll()
            return null
        }

    }

    private class DeleteFavAsyncTask internal constructor(private val mAsyncTaskDao: FavDAO?)
        : AsyncTask<FavImage?, Void?, Void?>() {
        override fun doInBackground(vararg params: FavImage?): Void? {
            mAsyncTaskDao?.deleteFavImage(params[0])
            return null
        }

    }

    private class GetFavListAsyncTask internal constructor(private val mAsyncTaskDao: FavDAO?)
        : AsyncTask<Void?, Void?, List<FavImage?>?>() {
        override fun doInBackground(vararg voids: Void?): List<FavImage?>? {
            return mAsyncTaskDao?.favAsList
        }

    }

    init {
        val db = FavRoomDatabase.getDatabase(application)
        favDAO = db?.favDAO()
        allFav = favDAO?.allFavImages
    }
}