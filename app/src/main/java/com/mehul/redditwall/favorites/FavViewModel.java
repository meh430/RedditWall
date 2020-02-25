package com.mehul.redditwall.favorites;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class FavViewModel extends AndroidViewModel {
    private FavRepository repo;
    private LiveData<List<FavImage>> fav;

    public FavViewModel(@NonNull Application application) {
        super(application);
        repo = new FavRepository(application.getApplicationContext());
        fav = repo.getAllFav();
    }

    public LiveData<List<FavImage>> getAllFav() {
        return fav;
    }

    public List<FavImage> getFavList() {
        return repo.getFavAsList();
    }

    public void deleteAll() {
        repo.deleteAll();
    }

    public void insert(FavImage saved) {
        repo.insert(saved);
    }

    public void deleteFavImage(FavImage saved) {
        repo.deleteFav(saved);
    }
}