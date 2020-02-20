package com.mehul.redditwall.savedsub;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class SubRepository {
    private SubDAO subDAO;
    private LiveData<List<SubSaved>> allSubs;

    SubRepository(Context application) {
        SubRoomDatabase db = SubRoomDatabase.getDatabase(application);
        subDAO = db.subDao();
        allSubs = subDAO.getAllSubSaved();
    }

    LiveData<List<SubSaved>> getAllSubSaved() {
        return allSubs;
    }

    void insert(SubSaved saved) {
        new insertAsyncTask(subDAO).execute(saved);
    }

    void deleteAll() {
        new deleteAllSubsAsyncTask(subDAO).execute();
    }

    void deleteSubSaved(SubSaved saved) {
        new deleteSubAsyncTask(subDAO).execute(saved);
    }

    private static class insertAsyncTask extends AsyncTask<SubSaved, Void, Void> {

        private SubDAO mAsyncTaskDao;

        insertAsyncTask(SubDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final SubSaved... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAllSubsAsyncTask extends AsyncTask<Void, Void, Void> {
        private SubDAO mAsyncTaskDao;

        deleteAllSubsAsyncTask(SubDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class deleteSubAsyncTask extends AsyncTask<SubSaved, Void, Void> {
        private SubDAO mAsyncTaskDao;

        deleteSubAsyncTask(SubDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final SubSaved... params) {
            mAsyncTaskDao.deleteSubSaved(params[0]);
            return null;
        }
    }
}
