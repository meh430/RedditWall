package com.mehul.redditwall.activities;

import java.lang.System;

@kotlin.Suppress(names = {"DEPRECATION"})
@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000x\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0011\u0010\u0014\u001a\u00020\u0015H\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0016J\b\u0010\u0017\u001a\u00020\u0018H\u0002J&\u0010\u0019\u001a\u00020\u00102\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u000e0\r2\u0006\u0010\u001b\u001a\u00020\b2\u0006\u0010\u001c\u001a\u00020\bH\u0002J\u0012\u0010\u001d\u001a\u00020\u00152\b\u0010\u001e\u001a\u0004\u0018\u00010\u001fH\u0014J\u0012\u0010 \u001a\u00020!2\b\u0010\"\u001a\u0004\u0018\u00010#H\u0016J\u0010\u0010$\u001a\u00020!2\u0006\u0010%\u001a\u00020&H\u0016J+\u0010\'\u001a\u00020\u00152\u0006\u0010(\u001a\u00020\b2\f\u0010)\u001a\b\u0012\u0004\u0012\u00020\u00100*2\u0006\u0010+\u001a\u00020,H\u0016\u00a2\u0006\u0002\u0010-J$\u0010.\u001a\b\u0012\u0004\u0012\u00020\u000e0\r2\u0006\u0010/\u001a\u00020\b2\f\u00100\u001a\b\u0012\u0004\u0012\u00020\u000e0\rH\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u00061"}, d2 = {"Lcom/mehul/redditwall/activities/HistoryActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "adapt", "Lcom/mehul/redditwall/adapters/HistAdapter;", "binding", "Lcom/mehul/redditwall/databinding/ActivityHistoryBinding;", "currSort", "", "height", "histViewModel", "Lcom/mehul/redditwall/viewmodels/HistViewModel;", "histories", "", "Lcom/mehul/redditwall/objects/HistoryItem;", "json", "", "uiScope", "Lkotlinx/coroutines/CoroutineScope;", "width", "downloadAllImages", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCon", "Landroid/content/Context;", "getJsonSlice", "hists", "start", "end", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateOptionsMenu", "", "menu", "Landroid/view/Menu;", "onOptionsItemSelected", "item", "Landroid/view/MenuItem;", "onRequestPermissionsResult", "requestCode", "permissions", "", "grantResults", "", "(I[Ljava/lang/String;[I)V", "sortList", "sort", "list", "app_debug"})
public final class HistoryActivity extends androidx.appcompat.app.AppCompatActivity {
    private kotlinx.coroutines.CoroutineScope uiScope;
    private int currSort = com.mehul.redditwall.R.id.recent;
    private java.lang.String json = "";
    private com.mehul.redditwall.adapters.HistAdapter adapt;
    private com.mehul.redditwall.viewmodels.HistViewModel histViewModel;
    private java.util.List<com.mehul.redditwall.objects.HistoryItem> histories;
    private int width = 1080;
    private int height = 1920;
    private com.mehul.redditwall.databinding.ActivityHistoryBinding binding;
    private java.util.HashMap _$_findViewCache;
    
    private final java.util.List<com.mehul.redditwall.objects.HistoryItem> sortList(int sort, java.util.List<com.mehul.redditwall.objects.HistoryItem> list) {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    public boolean onOptionsItemSelected(@org.jetbrains.annotations.NotNull()
    android.view.MenuItem item) {
        return false;
    }
    
    @java.lang.Override()
    public boolean onCreateOptionsMenu(@org.jetbrains.annotations.Nullable()
    android.view.Menu menu) {
        return false;
    }
    
    @java.lang.Override()
    public void onRequestPermissionsResult(int requestCode, @org.jetbrains.annotations.NotNull()
    java.lang.String[] permissions, @org.jetbrains.annotations.NotNull()
    int[] grantResults) {
    }
    
    private final java.lang.String getJsonSlice(java.util.List<com.mehul.redditwall.objects.HistoryItem> hists, int start, int end) {
        return null;
    }
    
    private final android.content.Context getCon() {
        return null;
    }
    
    public HistoryActivity() {
        super();
    }
}