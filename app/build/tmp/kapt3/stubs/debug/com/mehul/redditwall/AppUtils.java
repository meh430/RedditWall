package com.mehul.redditwall;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0005\u00a2\u0006\u0002\u0010\u0002\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0004"}, d2 = {"Lcom/mehul/redditwall/AppUtils;", "", "()V", "Companion", "app_debug"})
public final class AppUtils {
    public static final com.mehul.redditwall.AppUtils.Companion Companion = null;
    
    public AppUtils() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0015\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u0016\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u000bJ\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0006J\u0019\u0010\u000e\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u000bH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0010J\u0016\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\r2\u0006\u0010\u0013\u001a\u00020\rJ\u000e\u0010\u0014\u001a\u00020\u00152\u0006\u0010\t\u001a\u00020\u0006J\u0019\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u000bH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0010J\u000e\u0010\u0019\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0006J\u0016\u0010\u001a\u001a\u00020\u000b2\u0006\u0010\u001b\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0006\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u001c"}, d2 = {"Lcom/mehul/redditwall/AppUtils$Companion;", "", "()V", "getDimensions", "", "con", "Landroid/content/Context;", "getGridImageBitmap", "Landroid/graphics/Bitmap;", "context", "url", "", "getGridImageScale", "", "getJsonData", "endpoint", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getListRange", "p", "listLength", "getPreferences", "Landroid/content/SharedPreferences;", "getSubInfo", "Lorg/json/JSONObject;", "subName", "getWallDimensions", "saveBitmap", "bitmap", "app_debug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull()
        public final int[] getListRange(int p, int listLength) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final int[] getDimensions(@org.jetbrains.annotations.NotNull()
        android.content.Context con) {
            return null;
        }
        
        public final int getGridImageScale(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final int[] getWallDimensions(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.graphics.Bitmap getGridImageBitmap(@org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.NotNull()
        java.lang.String url) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.content.SharedPreferences getPreferences(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String saveBitmap(@org.jetbrains.annotations.NotNull()
        android.graphics.Bitmap bitmap, @org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Object getSubInfo(@org.jetbrains.annotations.NotNull()
        java.lang.String subName, @org.jetbrains.annotations.NotNull()
        kotlin.coroutines.Continuation<? super org.json.JSONObject> p1) {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Object getJsonData(@org.jetbrains.annotations.NotNull()
        java.lang.String endpoint, @org.jetbrains.annotations.NotNull()
        kotlin.coroutines.Continuation<? super java.lang.String> p1) {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}