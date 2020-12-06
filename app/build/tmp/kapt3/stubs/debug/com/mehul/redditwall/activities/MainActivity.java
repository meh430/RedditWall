package com.mehul.redditwall.activities;

import java.lang.System;

@android.annotation.SuppressLint(value = {"SetTextI18n"})
@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0086\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u0000 92\u00020\u00012\u00020\u0002:\u00019B\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u0019\u001a\u00020\u001aH\u0002J\u0010\u0010\u001b\u001a\u00020\u001a2\u0006\u0010\u001c\u001a\u00020\tH\u0002J\b\u0010\u001d\u001a\u00020\u001eH\u0002J\u0018\u0010\u001f\u001a\u0012\u0012\u0004\u0012\u00020\u000e0\rj\b\u0012\u0004\u0012\u00020\u000e` H\u0002JA\u0010!\u001a\u00020\u001a2\u0006\u0010\"\u001a\u00020\u001e2\u0006\u0010#\u001a\u00020\u000b2\u0006\u0010$\u001a\u00020%2\u0016\u0010&\u001a\u0012\u0012\u0004\u0012\u00020\u000e0\rj\b\u0012\u0004\u0012\u00020\u000e` H\u0083@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\'J\b\u0010(\u001a\u00020%H\u0002J\b\u0010)\u001a\u00020\u001aH\u0016J\u0010\u0010*\u001a\u00020\u001a2\u0006\u0010+\u001a\u00020,H\u0017J\u0012\u0010-\u001a\u00020\u001a2\b\u0010.\u001a\u0004\u0018\u00010/H\u0015J\u0010\u00100\u001a\u00020%2\u0006\u00101\u001a\u000202H\u0016J\b\u00103\u001a\u00020\u001aH\u0016J\u0010\u00104\u001a\u00020%2\u0006\u00105\u001a\u000206H\u0016J\b\u00107\u001a\u00020\u001aH\u0003J\u0010\u00108\u001a\u00020\u001a2\u0006\u0010+\u001a\u00020,H\u0007R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u000e0\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u000e0\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006:"}, d2 = {"Lcom/mehul/redditwall/activities/MainActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "Landroid/view/View$OnClickListener;", "()V", "adapter", "Lcom/mehul/redditwall/adapters/ImageAdapter;", "binding", "Lcom/mehul/redditwall/databinding/ActivityMainBinding;", "currentSort", "", "defaultLoad", "", "hotImages", "Ljava/util/ArrayList;", "Lcom/mehul/redditwall/objects/BitURL;", "imageJob", "Lkotlinx/coroutines/Job;", "newImages", "preferences", "Landroid/content/SharedPreferences;", "queryString", "scrollJob", "topImages", "uiScope", "Lkotlinx/coroutines/CoroutineScope;", "cancelThreads", "", "changeChipColor", "highlight", "getCon", "Landroid/content/Context;", "getList", "Lkotlin/collections/ArrayList;", "loadImages", "con", "query", "first", "", "images", "(Landroid/content/Context;Ljava/lang/String;ZLjava/util/ArrayList;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "networkAvailable", "onBackPressed", "onClick", "view", "Landroid/view/View;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateOptionsMenu", "menu", "Landroid/view/Menu;", "onDestroy", "onOptionsItemSelected", "item", "Landroid/view/MenuItem;", "runQuery", "startSearch", "Companion", "app_debug"})
public final class MainActivity extends androidx.appcompat.app.AppCompatActivity implements android.view.View.OnClickListener {
    private java.lang.String queryString = "";
    private java.lang.String defaultLoad = "";
    private java.util.ArrayList<com.mehul.redditwall.objects.BitURL> hotImages;
    private java.util.ArrayList<com.mehul.redditwall.objects.BitURL> topImages;
    private java.util.ArrayList<com.mehul.redditwall.objects.BitURL> newImages;
    private com.mehul.redditwall.adapters.ImageAdapter adapter;
    private kotlinx.coroutines.Job imageJob;
    private kotlinx.coroutines.Job scrollJob;
    private kotlinx.coroutines.CoroutineScope uiScope;
    private int currentSort = 0;
    private android.content.SharedPreferences preferences;
    private com.mehul.redditwall.databinding.ActivityMainBinding binding;
    private static boolean FIRST_RUN = true;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String SharedPrefFile = "com.mehul.redditwall";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String SAVED = "SAVED";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String OVERRIDE = "OVERRIDE";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String QUERY = "QUERY";
    public static final int NEW = 0;
    public static final int HOT = 1;
    public static final int TOP = 2;
    @org.jetbrains.annotations.Nullable()
    private static java.lang.String AFTER_NEW;
    @org.jetbrains.annotations.Nullable()
    private static java.lang.String AFTER_HOT;
    @org.jetbrains.annotations.Nullable()
    private static java.lang.String AFTER_TOP;
    public static final com.mehul.redditwall.activities.MainActivity.Companion Companion = null;
    private java.util.HashMap _$_findViewCache;
    
    @kotlinx.coroutines.InternalCoroutinesApi()
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void changeChipColor(int highlight) {
    }
    
    private final java.util.ArrayList<com.mehul.redditwall.objects.BitURL> getList() {
        return null;
    }
    
    private final android.content.Context getCon() {
        return null;
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    private final boolean networkAvailable() {
        return false;
    }
    
    @kotlinx.coroutines.InternalCoroutinesApi()
    public final void startSearch(@org.jetbrains.annotations.NotNull()
    android.view.View view) {
    }
    
    @java.lang.Override()
    public boolean onCreateOptionsMenu(@org.jetbrains.annotations.NotNull()
    android.view.Menu menu) {
        return false;
    }
    
    @java.lang.Override()
    public boolean onOptionsItemSelected(@org.jetbrains.annotations.NotNull()
    android.view.MenuItem item) {
        return false;
    }
    
    private final void cancelThreads() {
    }
    
    @kotlinx.coroutines.InternalCoroutinesApi()
    private final void runQuery() {
    }
    
    @java.lang.Override()
    public void onBackPressed() {
    }
    
    @kotlinx.coroutines.InternalCoroutinesApi()
    @java.lang.Override()
    public void onClick(@org.jetbrains.annotations.NotNull()
    android.view.View view) {
    }
    
    public MainActivity() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001c\u0010\t\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0006\"\u0004\b\u000b\u0010\bR\u001c\u0010\f\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u0006\"\u0004\b\u000e\u0010\bR\u001a\u0010\u000f\u001a\u00020\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u000e\u0010\u0015\u001a\u00020\u0016X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0016X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0016X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001d"}, d2 = {"Lcom/mehul/redditwall/activities/MainActivity$Companion;", "", "()V", "AFTER_HOT", "", "getAFTER_HOT", "()Ljava/lang/String;", "setAFTER_HOT", "(Ljava/lang/String;)V", "AFTER_NEW", "getAFTER_NEW", "setAFTER_NEW", "AFTER_TOP", "getAFTER_TOP", "setAFTER_TOP", "FIRST_RUN", "", "getFIRST_RUN", "()Z", "setFIRST_RUN", "(Z)V", "HOT", "", "NEW", "OVERRIDE", "QUERY", "SAVED", "SharedPrefFile", "TOP", "app_debug"})
    public static final class Companion {
        
        public final boolean getFIRST_RUN() {
            return false;
        }
        
        public final void setFIRST_RUN(boolean p0) {
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getAFTER_NEW() {
            return null;
        }
        
        public final void setAFTER_NEW(@org.jetbrains.annotations.Nullable()
        java.lang.String p0) {
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getAFTER_HOT() {
            return null;
        }
        
        public final void setAFTER_HOT(@org.jetbrains.annotations.Nullable()
        java.lang.String p0) {
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getAFTER_TOP() {
            return null;
        }
        
        public final void setAFTER_TOP(@org.jetbrains.annotations.Nullable()
        java.lang.String p0) {
        }
        
        private Companion() {
            super();
        }
    }
}