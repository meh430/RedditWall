package com.mehul.redditwall.objects;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B)\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\bJ\u0006\u0010\u0013\u001a\u00020\u0005J\u0006\u0010\u0014\u001a\u00020\nJ\u000e\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\nR\u0012\u0010\t\u001a\u00020\n8\u0002@\u0002X\u0083\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u0016\u0010\u0006\u001a\u00020\u00058\u0000X\u0081\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0016\u0010\u0007\u001a\u00020\u00058\u0000X\u0081\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0010R\u0016\u0010\u0004\u001a\u00020\u00058\u0000X\u0081\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0010\u00a8\u0006\u0018"}, d2 = {"Lcom/mehul/redditwall/objects/BitURL;", "", "img", "Landroid/graphics/Bitmap;", "url", "", "postLink", "previewUrl", "(Landroid/graphics/Bitmap;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "containsGif", "", "getImg", "()Landroid/graphics/Bitmap;", "setImg", "(Landroid/graphics/Bitmap;)V", "getPostLink$app_debug", "()Ljava/lang/String;", "getPreviewUrl$app_debug", "getUrl$app_debug", "getUrl", "hasGif", "setGif", "", "gif", "app_debug"})
public final class BitURL {
    @com.google.gson.annotations.SerializedName(value = "gif")
    @com.google.gson.annotations.Expose()
    private boolean containsGif = false;
    @org.jetbrains.annotations.Nullable()
    private android.graphics.Bitmap img;
    @org.jetbrains.annotations.NotNull()
    @com.google.gson.annotations.SerializedName(value = "url")
    @com.google.gson.annotations.Expose()
    private final java.lang.String url = null;
    @org.jetbrains.annotations.NotNull()
    @com.google.gson.annotations.SerializedName(value = "post")
    @com.google.gson.annotations.Expose()
    private final java.lang.String postLink = null;
    @org.jetbrains.annotations.NotNull()
    @com.google.gson.annotations.SerializedName(value = "preview_url")
    @com.google.gson.annotations.Expose()
    private final java.lang.String previewUrl = null;
    
    public final boolean hasGif() {
        return false;
    }
    
    public final void setGif(boolean gif) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getUrl() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.graphics.Bitmap getImg() {
        return null;
    }
    
    public final void setImg(@org.jetbrains.annotations.Nullable()
    android.graphics.Bitmap p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getUrl$app_debug() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPostLink$app_debug() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPreviewUrl$app_debug() {
        return null;
    }
    
    public BitURL(@org.jetbrains.annotations.Nullable()
    android.graphics.Bitmap img, @org.jetbrains.annotations.NotNull()
    java.lang.String url, @org.jetbrains.annotations.NotNull()
    java.lang.String postLink, @org.jetbrains.annotations.NotNull()
    java.lang.String previewUrl) {
        super();
    }
}