package com.mehul.redditwall.adapters;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0001\u001fB\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0002J\u0012\u0010\u0012\u001a\u0004\u0018\u00010\f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0002J\b\u0010\u0013\u001a\u00020\u0014H\u0016J\u000e\u0010\u0015\u001a\u00020\f2\u0006\u0010\u0016\u001a\u00020\u0014J\u001c\u0010\u0017\u001a\u00020\u00182\n\u0010\u0019\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0016\u001a\u00020\u0014H\u0016J\u001c\u0010\u001a\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u0014H\u0016J\u0014\u0010\u001e\u001a\u00020\u00182\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\f0\u000bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006 "}, d2 = {"Lcom/mehul/redditwall/adapters/SubAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/mehul/redditwall/adapters/SubAdapter$SubViewHolder;", "con", "Landroid/content/Context;", "vm", "Lcom/mehul/redditwall/viewmodels/SubViewModel;", "(Landroid/content/Context;Lcom/mehul/redditwall/viewmodels/SubViewModel;)V", "inflater", "Landroid/view/LayoutInflater;", "saves", "", "Lcom/mehul/redditwall/objects/Subreddit;", "subs", "alreadySaved", "", "name", "", "findSaved", "getItemCount", "", "getSubAtPosition", "position", "onBindViewHolder", "", "holder", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "setSubs", "SubViewHolder", "app_debug"})
public final class SubAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.mehul.redditwall.adapters.SubAdapter.SubViewHolder> {
    private final android.view.LayoutInflater inflater = null;
    private java.util.List<com.mehul.redditwall.objects.Subreddit> subs;
    private java.util.List<com.mehul.redditwall.objects.Subreddit> saves;
    private final android.content.Context con = null;
    private final com.mehul.redditwall.viewmodels.SubViewModel vm = null;
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.mehul.redditwall.adapters.SubAdapter.SubViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.mehul.redditwall.adapters.SubAdapter.SubViewHolder holder, int position) {
    }
    
    public final void setSubs(@org.jetbrains.annotations.NotNull()
    java.util.List<com.mehul.redditwall.objects.Subreddit> subs) {
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.mehul.redditwall.objects.Subreddit getSubAtPosition(int position) {
        return null;
    }
    
    private final boolean alreadySaved(java.lang.String name) {
        return false;
    }
    
    private final com.mehul.redditwall.objects.Subreddit findSaved(java.lang.String name) {
        return null;
    }
    
    public SubAdapter(@org.jetbrains.annotations.NotNull()
    android.content.Context con, @org.jetbrains.annotations.NotNull()
    com.mehul.redditwall.viewmodels.SubViewModel vm) {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0007R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/mehul/redditwall/adapters/SubAdapter$SubViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "itemView", "Landroid/view/View;", "(Lcom/mehul/redditwall/adapters/SubAdapter;Landroid/view/View;)V", "dateTv", "Landroid/widget/TextView;", "icon", "Landroid/widget/ImageView;", "subDescTv", "subNumTv", "subTv", "bindTo", "", "sub", "Lcom/mehul/redditwall/objects/Subreddit;", "app_debug"})
    public final class SubViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        private final android.widget.TextView subTv = null;
        private final android.widget.ImageView icon = null;
        private final android.widget.TextView subNumTv = null;
        private final android.widget.TextView subDescTv = null;
        private final android.widget.TextView dateTv = null;
        
        @android.annotation.SuppressLint(value = {"SetTextI18n"})
        public final void bindTo(@org.jetbrains.annotations.NotNull()
        com.mehul.redditwall.objects.Subreddit sub) {
        }
        
        public SubViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.View itemView) {
            super(null);
        }
    }
}