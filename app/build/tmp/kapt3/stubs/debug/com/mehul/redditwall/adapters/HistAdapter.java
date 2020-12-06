package com.mehul.redditwall.adapters;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0001\u0019B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J\u000e\u0010\f\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000eJ\b\u0010\u000f\u001a\u00020\u000eH\u0016J\u001c\u0010\u0010\u001a\u00020\u00112\n\u0010\u0012\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\r\u001a\u00020\u000eH\u0016J\u001c\u0010\u0013\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u000eH\u0016J\u0014\u0010\u0017\u001a\u00020\u00112\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\b0\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n \u000b*\u0004\u0018\u00010\n0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"Lcom/mehul/redditwall/adapters/HistAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/mehul/redditwall/adapters/HistAdapter$HistViewHolder;", "con", "Landroid/content/Context;", "(Landroid/content/Context;)V", "histories", "", "Lcom/mehul/redditwall/objects/HistoryItem;", "inflater", "Landroid/view/LayoutInflater;", "kotlin.jvm.PlatformType", "getHistory", "position", "", "getItemCount", "onBindViewHolder", "", "holder", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "setHistories", "hists", "HistViewHolder", "app_debug"})
public final class HistAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.mehul.redditwall.adapters.HistAdapter.HistViewHolder> {
    private final android.view.LayoutInflater inflater = null;
    private java.util.List<com.mehul.redditwall.objects.HistoryItem> histories;
    private final android.content.Context con = null;
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.mehul.redditwall.adapters.HistAdapter.HistViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    public final void setHistories(@org.jetbrains.annotations.NotNull()
    java.util.List<com.mehul.redditwall.objects.HistoryItem> hists) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.mehul.redditwall.objects.HistoryItem getHistory(int position) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.mehul.redditwall.adapters.HistAdapter.HistViewHolder holder, int position) {
    }
    
    public HistAdapter(@org.jetbrains.annotations.NotNull()
    android.content.Context con) {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/mehul/redditwall/adapters/HistAdapter$HistViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "itemView", "Landroid/view/View;", "(Lcom/mehul/redditwall/adapters/HistAdapter;Landroid/view/View;)V", "dateTv", "Landroid/widget/TextView;", "img", "Landroid/widget/ImageView;", "nameTv", "sourceTv", "bindTo", "", "history", "Lcom/mehul/redditwall/objects/HistoryItem;", "app_debug"})
    public final class HistViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        private final android.widget.TextView nameTv = null;
        private final android.widget.TextView sourceTv = null;
        private final android.widget.TextView dateTv = null;
        private final android.widget.ImageView img = null;
        
        public final void bindTo(@org.jetbrains.annotations.NotNull()
        com.mehul.redditwall.objects.HistoryItem history) {
        }
        
        public HistViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.View itemView) {
            super(null);
        }
    }
}