package com.mehul.redditwall.favorites;


import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class FavRepository {
    private FavDAO favDAO;
    private LiveData<List<FavImage>> allFav;

    FavRepository(Context application) {
        FavRoomDatabase db = FavRoomDatabase.getDatabase(application);
        favDAO = db.favDAO();
        allFav = favDAO.getAllFavImages();
    }

    LiveData<List<FavImage>> getAllFav() {
        return allFav;
    }

    List<FavImage> getFavAsList() {
        try {
            return new getFavListAsyncTask(favDAO).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    void insert(FavImage saved) {
        new insertAsyncTask(favDAO).execute(saved);
    }

    void deleteAll() {
        new deleteAllFavAsyncTask(favDAO).execute();
    }

    void deleteFav(FavImage saved) {
        new deleteFavAsyncTask(favDAO).execute(saved);
    }

    private static class insertAsyncTask extends AsyncTask<FavImage, Void, Void> {

        private FavDAO mAsyncTaskDao;

        insertAsyncTask(FavDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final FavImage... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAllFavAsyncTask extends AsyncTask<Void, Void, Void> {
        private FavDAO mAsyncTaskDao;

        deleteAllFavAsyncTask(FavDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class deleteFavAsyncTask extends AsyncTask<FavImage, Void, Void> {
        private FavDAO mAsyncTaskDao;

        deleteFavAsyncTask(FavDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final FavImage... params) {
            mAsyncTaskDao.deleteFavImage(params[0]);
            return null;
        }
    }

    private static class getFavListAsyncTask extends AsyncTask<Void, Void, List<FavImage>> {
        private FavDAO mAsyncTaskDao;

        getFavListAsyncTask(FavDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected List<FavImage> doInBackground(Void... voids) {
            return mAsyncTaskDao.getFavAsList();
        }
    }
}
