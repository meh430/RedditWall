package com.mehul.redditwall.databases;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001:\u0001\u0017B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0011\u0010\u0010\u001a\u00020\u0011H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0012J\u0019\u0010\u0013\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\bH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0015J\u0019\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\bH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0015R\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\b0\u00078F\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\rR\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0018"}, d2 = {"Lcom/mehul/redditwall/databases/FavRepository;", "", "application", "Landroid/content/Context;", "(Landroid/content/Context;)V", "allFav", "Landroidx/lifecycle/LiveData;", "", "Lcom/mehul/redditwall/objects/FavImage;", "getAllFav", "()Landroidx/lifecycle/LiveData;", "favAsList", "getFavAsList", "()Ljava/util/List;", "favDAO", "Lcom/mehul/redditwall/databases/FavDAO;", "deleteAll", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteFav", "saved", "(Lcom/mehul/redditwall/objects/FavImage;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insert", "GetFavListAsyncTask", "app_debug"})
public final class FavRepository {
    private final com.mehul.redditwall.databases.FavDAO favDAO = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.util.List<com.mehul.redditwall.objects.FavImage>> allFav = null;
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.mehul.redditwall.objects.FavImage>> getAllFav() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.mehul.redditwall.objects.FavImage> getFavAsList() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    com.mehul.redditwall.objects.FavImage saved, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> p1) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteAll(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> p0) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteFav(@org.jetbrains.annotations.NotNull()
    com.mehul.redditwall.objects.FavImage saved, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> p1) {
        return null;
    }
    
    public FavRepository(@org.jetbrains.annotations.NotNull()
    android.content.Context application) {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\b\u0002\b\u0002\u0018\u00002\u001a\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\u00030\u0001B\u000f\b\u0000\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\'\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0012\u0010\t\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00020\n\"\u00020\u0002H\u0014\u00a2\u0006\u0002\u0010\u000bR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcom/mehul/redditwall/databases/FavRepository$GetFavListAsyncTask;", "Landroid/os/AsyncTask;", "Ljava/lang/Void;", "", "Lcom/mehul/redditwall/objects/FavImage;", "mAsyncTaskDao", "Lcom/mehul/redditwall/databases/FavDAO;", "(Lcom/mehul/redditwall/databases/FavDAO;)V", "doInBackground", "voids", "", "([Ljava/lang/Void;)Ljava/util/List;", "app_debug"})
    static final class GetFavListAsyncTask extends android.os.AsyncTask<java.lang.Void, java.lang.Void, java.util.List<? extends com.mehul.redditwall.objects.FavImage>> {
        private final com.mehul.redditwall.databases.FavDAO mAsyncTaskDao = null;
        
        @org.jetbrains.annotations.NotNull()
        @java.lang.Override()
        protected java.util.List<com.mehul.redditwall.objects.FavImage> doInBackground(@org.jetbrains.annotations.NotNull()
        java.lang.Void... voids) {
            return null;
        }
        
        public GetFavListAsyncTask(@org.jetbrains.annotations.NotNull()
        com.mehul.redditwall.databases.FavDAO mAsyncTaskDao) {
            super();
        }
    }
}