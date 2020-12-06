package com.mehul.redditwall.databases;

import java.lang.System;

@androidx.room.Dao()
@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0004\bg\u0018\u00002\u00020\u0001J\b\u0010\u000b\u001a\u00020\fH\'J\u0010\u0010\r\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u0005H\'J\u0010\u0010\u000f\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u0005H\'R \u0010\u0002\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00038gX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007R\u001a\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\u00048gX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0010"}, d2 = {"Lcom/mehul/redditwall/databases/FavDAO;", "", "allFavImages", "Landroidx/lifecycle/LiveData;", "", "Lcom/mehul/redditwall/objects/FavImage;", "getAllFavImages", "()Landroidx/lifecycle/LiveData;", "favAsList", "getFavAsList", "()Ljava/util/List;", "deleteAll", "", "deleteFavImage", "saved", "insert", "app_debug"})
public abstract interface FavDAO {
    
    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    public abstract void insert(@org.jetbrains.annotations.NotNull()
    com.mehul.redditwall.objects.FavImage saved);
    
    @androidx.room.Query(value = "DELETE FROM fav_table")
    public abstract void deleteAll();
    
    @org.jetbrains.annotations.NotNull()
    @androidx.room.Query(value = "SELECT * from fav_table ORDER BY fav_url ASC")
    public abstract androidx.lifecycle.LiveData<java.util.List<com.mehul.redditwall.objects.FavImage>> getAllFavImages();
    
    @org.jetbrains.annotations.NotNull()
    @androidx.room.Query(value = "SELECT * from fav_table ORDER BY fav_url ASC")
    public abstract java.util.List<com.mehul.redditwall.objects.FavImage> getFavAsList();
    
    @androidx.room.Delete()
    public abstract void deleteFavImage(@org.jetbrains.annotations.NotNull()
    com.mehul.redditwall.objects.FavImage saved);
}