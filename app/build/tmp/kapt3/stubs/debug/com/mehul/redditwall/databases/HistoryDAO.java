package com.mehul.redditwall.databases;

import java.lang.System;

@androidx.room.Dao()
@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\bg\u0018\u00002\u00020\u0001J\b\u0010\f\u001a\u00020\rH\'J\u0010\u0010\u000e\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u0005H\'J\u0010\u0010\u0010\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u0005H\'R \u0010\u0002\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00038gX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007R\u001a\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\t8gX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0011"}, d2 = {"Lcom/mehul/redditwall/databases/HistoryDAO;", "", "allHistoryItems", "Landroidx/lifecycle/LiveData;", "", "Lcom/mehul/redditwall/objects/HistoryItem;", "getAllHistoryItems", "()Landroidx/lifecycle/LiveData;", "anyHistoryItem", "", "getAnyHistoryItem", "()[Lcom/mehul/redditwall/objects/HistoryItem;", "deleteAll", "", "deleteHistoryItem", "history", "insert", "app_debug"})
public abstract interface HistoryDAO {
    
    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.IGNORE)
    public abstract void insert(@org.jetbrains.annotations.NotNull()
    com.mehul.redditwall.objects.HistoryItem history);
    
    @androidx.room.Query(value = "DELETE FROM hist_table")
    public abstract void deleteAll();
    
    @org.jetbrains.annotations.NotNull()
    @androidx.room.Query(value = "SELECT * from hist_table ORDER BY hist_name ASC")
    public abstract androidx.lifecycle.LiveData<java.util.List<com.mehul.redditwall.objects.HistoryItem>> getAllHistoryItems();
    
    @org.jetbrains.annotations.NotNull()
    @androidx.room.Query(value = "SELECT * from hist_table LIMIT 1")
    public abstract com.mehul.redditwall.objects.HistoryItem[] getAnyHistoryItem();
    
    @androidx.room.Delete()
    public abstract void deleteHistoryItem(@org.jetbrains.annotations.NotNull()
    com.mehul.redditwall.objects.HistoryItem history);
}