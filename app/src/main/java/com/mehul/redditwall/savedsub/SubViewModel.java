package com.mehul.redditwall.savedsub;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class SubViewModel extends AndroidViewModel {
    private SubRepository repo;
    private LiveData<List<SubSaved>> subs;

    public SubViewModel(@NonNull Application application) {
        super(application);
        repo = new SubRepository(application.getApplicationContext());
        subs = repo.getAllSubSaved();
    }

    public LiveData<List<SubSaved>> getAllSubs() {
        return subs;
    }

    public void deleteAll() {
        repo.deleteAll();
    }

    public void insert(SubSaved saved) {
        repo.insert(saved);
    }

    public void deleteSavedSub(SubSaved saved) {
        repo.deleteSubSaved(saved);
    }
}
