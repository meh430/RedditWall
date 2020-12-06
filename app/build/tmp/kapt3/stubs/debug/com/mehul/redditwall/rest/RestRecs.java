package com.mehul.redditwall.rest;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0011\u0010\u0005\u001a\u00020\u0004H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u001f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\n\u001a\u00020\u0004H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\f"}, d2 = {"Lcom/mehul/redditwall/rest/RestRecs;", "", "()V", "ENDPOINT", "", "getRecsJSON", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "parseJSON", "Ljava/util/ArrayList;", "Lcom/mehul/redditwall/objects/Recommendation;", "json", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class RestRecs {
    private static final java.lang.String ENDPOINT = "https://reddtwalls-8176.restdb.io/rest/recommendations";
    public static final com.mehul.redditwall.rest.RestRecs INSTANCE = null;
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getRecsJSON(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.String> p0) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object parseJSON(@org.jetbrains.annotations.NotNull()
    java.lang.String json, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.ArrayList<com.mehul.redditwall.objects.Recommendation>> p1) {
        return null;
    }
    
    private RestRecs() {
        super();
    }
}