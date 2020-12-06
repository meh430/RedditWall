package com.mehul.redditwall.adapters;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0001\"B%\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0016\u0010\u0005\u001a\u0012\u0012\u0004\u0012\u00020\u00070\u0006j\b\u0012\u0004\u0012\u00020\u0007`\b\u00a2\u0006\u0002\u0010\tJ\u0016\u0010\u0016\u001a\u0012\u0012\u0004\u0012\u00020\u00070\u0006j\b\u0012\u0004\u0012\u00020\u0007`\bJ\b\u0010\u0017\u001a\u00020\u000fH\u0016J\u001c\u0010\u0018\u001a\u00020\u00192\n\u0010\u001a\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u001b\u001a\u00020\u000fH\u0016J\u001c\u0010\u001c\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u000fH\u0016J.\u0010 \u001a\u00020\u00192\u0016\u0010\r\u001a\u0012\u0012\u0004\u0012\u00020\u00070\u0006j\b\u0012\u0004\u0012\u00020\u0007`\b2\u000e\u0010!\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000bJ\u0016\u0010 \u001a\u00020\u00192\u000e\u0010!\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006#"}, d2 = {"Lcom/mehul/redditwall/adapters/FavAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/mehul/redditwall/adapters/FavAdapter$FavViewHolder;", "con", "Landroid/content/Context;", "lis", "Ljava/util/ArrayList;", "Lcom/mehul/redditwall/objects/BitURL;", "Lkotlin/collections/ArrayList;", "(Landroid/content/Context;Ljava/util/ArrayList;)V", "favList", "", "Lcom/mehul/redditwall/objects/FavImage;", "favs", "height", "", "inflater", "Landroid/view/LayoutInflater;", "lowRes", "", "scale", "width", "getBitList", "getItemCount", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "setFavs", "favLists", "FavViewHolder", "app_debug"})
public final class FavAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.mehul.redditwall.adapters.FavAdapter.FavViewHolder> {
    private final android.view.LayoutInflater inflater = null;
    private java.util.List<com.mehul.redditwall.objects.FavImage> favList;
    private java.util.ArrayList<com.mehul.redditwall.objects.BitURL> favs;
    private final int width = 0;
    private final int height = 0;
    private final int scale = 0;
    private final boolean lowRes = false;
    private final android.content.Context con = null;
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.mehul.redditwall.adapters.FavAdapter.FavViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.mehul.redditwall.adapters.FavAdapter.FavViewHolder holder, int position) {
    }
    
    public final void setFavs(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<com.mehul.redditwall.objects.BitURL> favs, @org.jetbrains.annotations.NotNull()
    java.util.List<com.mehul.redditwall.objects.FavImage> favLists) {
    }
    
    public final void setFavs(@org.jetbrains.annotations.NotNull()
    java.util.List<com.mehul.redditwall.objects.FavImage> favLists) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.ArrayList<com.mehul.redditwall.objects.BitURL> getBitList() {
        return null;
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    public FavAdapter(@org.jetbrains.annotations.NotNull()
    android.content.Context con, @org.jetbrains.annotations.NotNull()
    java.util.ArrayList<com.mehul.redditwall.objects.BitURL> lis) {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/mehul/redditwall/adapters/FavAdapter$FavViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "itemView", "Landroid/view/View;", "(Lcom/mehul/redditwall/adapters/FavAdapter;Landroid/view/View;)V", "img", "Landroid/widget/ImageView;", "bindTo", "", "saved", "Lcom/mehul/redditwall/objects/BitURL;", "p", "", "app_debug"})
    public final class FavViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        private final android.widget.ImageView img = null;
        
        public final void bindTo(@org.jetbrains.annotations.NotNull()
        com.mehul.redditwall.objects.BitURL saved, int p) {
        }
        
        public FavViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.View itemView) {
            super(null);
        }
    }
}