package com.mehul.redditwall.rest;

import java.lang.System;

@kotlin.Suppress(names = {"LocalVariableName", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"})
@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\b\u0000\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0005\u00a2\u0006\u0002\u0010\u0002\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0004"}, d2 = {"Lcom/mehul/redditwall/rest/QueryRequest;", "", "()V", "Companion", "app_debug"})
public final class QueryRequest {
    private static int sort = 0;
    public static final com.mehul.redditwall.rest.QueryRequest.Companion Companion = null;
    
    public QueryRequest() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J+\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\fJW\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u00062\b\u0010\u0010\u001a\u0004\u0018\u00010\u00112\u0006\u0010\n\u001a\u00020\u000b2\u0016\u0010\u0012\u001a\u0012\u0012\u0004\u0012\u00020\u00140\u0013j\b\u0012\u0004\u0012\u00020\u0014`\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u00172\u0006\u0010\b\u001a\u00020\tH\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0018R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0019"}, d2 = {"Lcom/mehul/redditwall/rest/QueryRequest$Companion;", "", "()V", "sort", "", "getQueryJson", "", "QUERY", "first", "", "context", "Landroid/content/Context;", "(Ljava/lang/String;ZLandroid/content/Context;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "loadImgsFromJSON", "", "jsonResult", "adapter", "Lcom/mehul/redditwall/adapters/ImageAdapter;", "images", "Ljava/util/ArrayList;", "Lcom/mehul/redditwall/objects/BitURL;", "Lkotlin/collections/ArrayList;", "load", "Landroid/widget/ProgressBar;", "(Ljava/lang/String;Lcom/mehul/redditwall/adapters/ImageAdapter;Landroid/content/Context;Ljava/util/ArrayList;Landroid/widget/ProgressBar;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.Nullable()
        @kotlinx.coroutines.InternalCoroutinesApi()
        public final java.lang.Object getQueryJson(@org.jetbrains.annotations.NotNull()
        java.lang.String QUERY, boolean first, @org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.NotNull()
        kotlin.coroutines.Continuation<? super java.lang.String> p3) {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        @kotlinx.coroutines.InternalCoroutinesApi()
        public final java.lang.Object loadImgsFromJSON(@org.jetbrains.annotations.Nullable()
        java.lang.String jsonResult, @org.jetbrains.annotations.Nullable()
        com.mehul.redditwall.adapters.ImageAdapter adapter, @org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.NotNull()
        java.util.ArrayList<com.mehul.redditwall.objects.BitURL> images, @org.jetbrains.annotations.Nullable()
        android.widget.ProgressBar load, boolean first, @org.jetbrains.annotations.NotNull()
        kotlin.coroutines.Continuation<? super kotlin.Unit> p6) {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}