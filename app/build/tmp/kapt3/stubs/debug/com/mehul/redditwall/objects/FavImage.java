package com.mehul.redditwall.objects;

import java.lang.System;

@androidx.room.Entity(tableName = "fav_table")
@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0019\b\u0007\u0018\u00002\u00020\u0001B?\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\u0005\u0012\u0006\u0010\t\u001a\u00020\u0005\u0012\u0006\u0010\n\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\fR\u001e\u0010\t\u001a\u00020\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001e\u0010\u0004\u001a\u00020\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u000e\"\u0004\b\u0012\u0010\u0010R\u001e\u0010\u0002\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u001e\u0010\u0006\u001a\u00020\u00078\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0017\"\u0004\b\u0018\u0010\u0019R\u001e\u0010\b\u001a\u00020\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u000e\"\u0004\b\u001b\u0010\u0010R\u001e\u0010\n\u001a\u00020\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u000e\"\u0004\b\u001d\u0010\u0010R\u001a\u0010\u000b\u001a\u00020\u0007X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u0017\"\u0004\b\u001f\u0010\u0019\u00a8\u0006 "}, d2 = {"Lcom/mehul/redditwall/objects/FavImage;", "", "id", "", "favUrl", "", "isGif", "", "postLink", "favName", "previewUrl", "selected", "(ILjava/lang/String;ZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)V", "getFavName", "()Ljava/lang/String;", "setFavName", "(Ljava/lang/String;)V", "getFavUrl", "setFavUrl", "getId", "()I", "setId", "(I)V", "()Z", "setGif", "(Z)V", "getPostLink", "setPostLink", "getPreviewUrl", "setPreviewUrl", "getSelected", "setSelected", "app_debug"})
public final class FavImage {
    @androidx.room.PrimaryKey(autoGenerate = true)
    private int id;
    @org.jetbrains.annotations.NotNull()
    @androidx.room.ColumnInfo(name = "fav_url")
    @com.google.gson.annotations.SerializedName(value = "url")
    @com.google.gson.annotations.Expose()
    private java.lang.String favUrl;
    @androidx.room.ColumnInfo(name = "fav_gif")
    @com.google.gson.annotations.SerializedName(value = "gif")
    @com.google.gson.annotations.Expose()
    private boolean isGif;
    @org.jetbrains.annotations.NotNull()
    @androidx.room.ColumnInfo(name = "fav_post_link")
    @com.google.gson.annotations.SerializedName(value = "post")
    @com.google.gson.annotations.Expose()
    private java.lang.String postLink;
    @org.jetbrains.annotations.NotNull()
    @androidx.room.ColumnInfo(name = "fav_name")
    @com.google.gson.annotations.SerializedName(value = "fav_name")
    private java.lang.String favName;
    @org.jetbrains.annotations.NotNull()
    @androidx.room.ColumnInfo(name = "preview_url")
    @com.google.gson.annotations.SerializedName(value = "preview_url")
    @com.google.gson.annotations.Expose()
    private java.lang.String previewUrl;
    private boolean selected;
    
    public final int getId() {
        return 0;
    }
    
    public final void setId(int p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getFavUrl() {
        return null;
    }
    
    public final void setFavUrl(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    public final boolean isGif() {
        return false;
    }
    
    public final void setGif(boolean p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPostLink() {
        return null;
    }
    
    public final void setPostLink(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getFavName() {
        return null;
    }
    
    public final void setFavName(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPreviewUrl() {
        return null;
    }
    
    public final void setPreviewUrl(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    public final boolean getSelected() {
        return false;
    }
    
    public final void setSelected(boolean p0) {
    }
    
    public FavImage(int id, @org.jetbrains.annotations.NotNull()
    java.lang.String favUrl, boolean isGif, @org.jetbrains.annotations.NotNull()
    java.lang.String postLink, @org.jetbrains.annotations.NotNull()
    java.lang.String favName, @org.jetbrains.annotations.NotNull()
    java.lang.String previewUrl, boolean selected) {
        super();
    }
}