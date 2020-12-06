package com.mehul.redditwall.activities;

import java.lang.System;

@kotlin.Suppress(names = {"PrivatePropertyName", "DEPRECATION", "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "LocalVariableName"})
@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u00ca\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0017\u0018\u0000 q2\u00020\u00012\u00020\u0002:\u0001qB\u0005\u00a2\u0006\u0002\u0010\u0003J\u0010\u00102\u001a\u0002032\b\u00104\u001a\u0004\u0018\u000105J\u0010\u00106\u001a\u00020\u000e2\u0006\u00107\u001a\u000208H\u0002J\b\u00109\u001a\u000203H\u0002J\u000e\u0010:\u001a\u0002032\u0006\u00104\u001a\u000205J\u000e\u0010;\u001a\u0002032\u0006\u00104\u001a\u000205J\b\u0010<\u001a\u00020=H\u0002J\u000e\u0010>\u001a\u0002032\u0006\u00104\u001a\u000205J\u000e\u0010?\u001a\u0002032\u0006\u00104\u001a\u000205J\u000e\u0010@\u001a\u0002032\u0006\u00104\u001a\u000205J#\u0010A\u001a\u0002032\b\u0010B\u001a\u0004\u0018\u00010=2\u0006\u0010C\u001a\u00020\u000eH\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010DJ\u0011\u0010E\u001a\u000203H\u0083@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010FJ\b\u0010G\u001a\u000203H\u0016J\u0012\u0010H\u001a\u0002032\b\u0010I\u001a\u0004\u0018\u00010JH\u0014J\b\u0010K\u001a\u000203H\u0016J\u0010\u0010L\u001a\u00020\u00072\u0006\u0010M\u001a\u00020NH\u0016J(\u0010O\u001a\u00020\u00072\u0006\u0010P\u001a\u00020N2\u0006\u0010Q\u001a\u00020N2\u0006\u0010R\u001a\u00020S2\u0006\u0010T\u001a\u00020SH\u0016J\u0010\u0010U\u001a\u0002032\u0006\u0010M\u001a\u00020NH\u0016J+\u0010V\u001a\u0002032\u0006\u0010W\u001a\u00020\u001f2\f\u0010X\u001a\b\u0012\u0004\u0012\u00020\u000e0Y2\u0006\u0010Z\u001a\u00020[H\u0016\u00a2\u0006\u0002\u0010\\J(\u0010]\u001a\u00020\u00072\u0006\u0010M\u001a\u00020N2\u0006\u0010^\u001a\u00020N2\u0006\u0010_\u001a\u00020S2\u0006\u0010`\u001a\u00020SH\u0016J\u0010\u0010a\u001a\u0002032\u0006\u0010M\u001a\u00020NH\u0016J\u0010\u0010b\u001a\u00020\u00072\u0006\u0010M\u001a\u00020NH\u0016J\b\u0010c\u001a\u000203H\u0016J\u0010\u0010d\u001a\u00020\u00072\u0006\u0010e\u001a\u00020NH\u0016J\b\u0010f\u001a\u000203H\u0002J\b\u0010g\u001a\u000203H\u0002J\u0010\u0010h\u001a\u0002032\u0006\u00104\u001a\u000205H\u0007J\u0019\u0010i\u001a\u0002032\u0006\u0010B\u001a\u00020=H\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010jJ\b\u0010k\u001a\u000203H\u0002J\b\u0010l\u001a\u000203H\u0002J\u0010\u0010m\u001a\u0002032\u0006\u0010n\u001a\u00020\u0007H\u0002J\u000e\u0010o\u001a\u0002032\u0006\u00104\u001a\u000205J\b\u0010p\u001a\u000203H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u0018X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0019\u001a\u0012\u0012\u0004\u0012\u00020\u001b0\u001aj\b\u0012\u0004\u0012\u00020\u001b`\u001cX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010!\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010#\u001a\u00020$8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b%\u0010&R\u0010\u0010\'\u001a\u0004\u0018\u00010(X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010)\u001a\u0004\u0018\u00010\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020,X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010.\u001a\u0004\u0018\u00010/X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00100\u001a\u000201X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006r"}, d2 = {"Lcom/mehul/redditwall/activities/WallActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "Landroid/view/GestureDetector$OnGestureListener;", "()V", "binding", "Lcom/mehul/redditwall/databinding/ActivityWallBinding;", "bottomUp", "", "currentBitmap", "Landroid/graphics/Bitmap;", "detector", "Landroid/view/GestureDetector;", "downloadOriginal", "fName", "", "favViewModel", "Lcom/mehul/redditwall/viewmodels/FavViewModel;", "filledHeart", "Landroid/graphics/drawable/Drawable;", "fromFav", "fromHist", "histViewModel", "Lcom/mehul/redditwall/viewmodels/HistViewModel;", "imageJob", "Lkotlinx/coroutines/Job;", "imageList", "Ljava/util/ArrayList;", "Lcom/mehul/redditwall/objects/BitURL;", "Lkotlin/collections/ArrayList;", "imgUrl", "index", "", "isGif", "jsonList", "noQuery", "notificationBuilder", "Landroidx/core/app/NotificationCompat$Builder;", "getNotificationBuilder", "()Landroidx/core/app/NotificationCompat$Builder;", "notifyManager", "Landroid/app/NotificationManager;", "openHeart", "postLink", "preferences", "Landroid/content/SharedPreferences;", "query", "starred", "Landroid/view/Menu;", "uiScope", "Lkotlinx/coroutines/CoroutineScope;", "backPress", "", "view", "Landroid/view/View;", "convertUTC", "utc", "", "createNotificationChannel", "downloadImage", "favoriteImage", "getCon", "Landroid/content/Context;", "launchPost", "launchSearch", "launchUser", "loadImages", "con", "queryString", "(Landroid/content/Context;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "loadUps", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onDown", "motionEvent", "Landroid/view/MotionEvent;", "onFling", "e1", "e2", "vX", "", "vY", "onLongPress", "onRequestPermissionsResult", "requestCode", "permissions", "", "grantResults", "", "(I[Ljava/lang/String;[I)V", "onScroll", "motionEvent1", "v", "v1", "onShowPress", "onSingleTapUp", "onStop", "onTouchEvent", "event", "saveImage", "sendNotification", "setWallpaper", "startUp", "(Landroid/content/Context;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "swipedLeft", "swipedRight", "toggle", "show", "toggleBottom", "updateNotification", "Companion", "app_debug"})
public final class WallActivity extends androidx.appcompat.app.AppCompatActivity implements android.view.GestureDetector.OnGestureListener {
    private java.lang.String jsonList = "";
    private java.lang.String fName = "";
    private java.lang.String imgUrl = "";
    private java.lang.String postLink = "";
    private java.lang.String query = "";
    private boolean bottomUp = false;
    private boolean isGif = false;
    private boolean downloadOriginal = false;
    private boolean fromFav = false;
    private boolean fromHist = false;
    private boolean noQuery = false;
    private android.graphics.drawable.Drawable filledHeart;
    private android.graphics.drawable.Drawable openHeart;
    private int index = 0;
    private java.util.ArrayList<com.mehul.redditwall.objects.BitURL> imageList;
    private android.view.GestureDetector detector;
    private kotlinx.coroutines.Job imageJob;
    private android.view.Menu starred;
    private com.mehul.redditwall.viewmodels.FavViewModel favViewModel;
    private com.mehul.redditwall.viewmodels.HistViewModel histViewModel;
    private final kotlinx.coroutines.CoroutineScope uiScope = null;
    private android.graphics.Bitmap currentBitmap;
    private android.content.SharedPreferences preferences;
    private com.mehul.redditwall.databinding.ActivityWallBinding binding;
    private android.app.NotificationManager notifyManager;
    private static final java.lang.String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private static final int NOTIFICATION_ID = 0;
    public static final int WRITE = 1231;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String WALL_URL = "WALLURL";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String GIF = "GIF";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String LIST = "LIST";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String INDEX = "INDEX";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FROM_FAV = "FAV_IMAGES";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FROM_HIST = "HIST_IMAGES";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String FAV_LIST = "FAV_LIST";
    @org.jetbrains.annotations.Nullable()
    private static java.lang.String AFTER_NEW_WALL;
    @org.jetbrains.annotations.Nullable()
    private static java.lang.String AFTER_HOT_WALL;
    @org.jetbrains.annotations.Nullable()
    private static java.lang.String AFTER_TOP_WALL;
    public static final com.mehul.redditwall.activities.WallActivity.Companion Companion = null;
    private java.util.HashMap _$_findViewCache;
    
    private final androidx.core.app.NotificationCompat.Builder getNotificationBuilder() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @android.annotation.SuppressLint(value = {"NewApi"})
    public final void setWallpaper(@org.jetbrains.annotations.NotNull()
    android.view.View view) {
    }
    
    public final void launchPost(@org.jetbrains.annotations.NotNull()
    android.view.View view) {
    }
    
    public final void downloadImage(@org.jetbrains.annotations.NotNull()
    android.view.View view) {
    }
    
    private final void saveImage() {
    }
    
    @java.lang.Override()
    public void onRequestPermissionsResult(int requestCode, @org.jetbrains.annotations.NotNull()
    java.lang.String[] permissions, @org.jetbrains.annotations.NotNull()
    int[] grantResults) {
    }
    
    private final void sendNotification() {
    }
    
    private final void updateNotification() {
    }
    
    private final void createNotificationChannel() {
    }
    
    @java.lang.Override()
    public void onStop() {
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    @java.lang.Override()
    public boolean onTouchEvent(@org.jetbrains.annotations.NotNull()
    android.view.MotionEvent event) {
        return false;
    }
    
    private final android.content.Context getCon() {
        return null;
    }
    
    private final void swipedRight() {
    }
    
    private final void swipedLeft() {
    }
    
    @java.lang.Override()
    public boolean onFling(@org.jetbrains.annotations.NotNull()
    android.view.MotionEvent e1, @org.jetbrains.annotations.NotNull()
    android.view.MotionEvent e2, float vX, float vY) {
        return false;
    }
    
    @java.lang.Override()
    public boolean onDown(@org.jetbrains.annotations.NotNull()
    android.view.MotionEvent motionEvent) {
        return false;
    }
    
    @java.lang.Override()
    public void onShowPress(@org.jetbrains.annotations.NotNull()
    android.view.MotionEvent motionEvent) {
    }
    
    @java.lang.Override()
    public boolean onSingleTapUp(@org.jetbrains.annotations.NotNull()
    android.view.MotionEvent motionEvent) {
        return false;
    }
    
    @java.lang.Override()
    public boolean onScroll(@org.jetbrains.annotations.NotNull()
    android.view.MotionEvent motionEvent, @org.jetbrains.annotations.NotNull()
    android.view.MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
    
    @java.lang.Override()
    public void onLongPress(@org.jetbrains.annotations.NotNull()
    android.view.MotionEvent motionEvent) {
    }
    
    private final java.lang.String convertUTC(long utc) {
        return null;
    }
    
    public final void launchSearch(@org.jetbrains.annotations.NotNull()
    android.view.View view) {
    }
    
    public final void toggleBottom(@org.jetbrains.annotations.NotNull()
    android.view.View view) {
    }
    
    @java.lang.Override()
    public void onBackPressed() {
    }
    
    private final void toggle(boolean show) {
    }
    
    public final void launchUser(@org.jetbrains.annotations.NotNull()
    android.view.View view) {
    }
    
    public final void favoriteImage(@org.jetbrains.annotations.NotNull()
    android.view.View view) {
    }
    
    public final void backPress(@org.jetbrains.annotations.Nullable()
    android.view.View view) {
    }
    
    public WallActivity() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0011\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J)\u0010\u001a\u001a\u0012\u0012\u0004\u0012\u00020\u001c0\u001bj\b\u0012\u0004\u0012\u00020\u001c`\u001d2\u0006\u0010\u001e\u001a\u00020\u0004H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001fR\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001c\u0010\t\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0006\"\u0004\b\u000b\u0010\bR\u001c\u0010\f\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u0006\"\u0004\b\u000e\u0010\bR\u000e\u0010\u000f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0016X\u0086T\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006 "}, d2 = {"Lcom/mehul/redditwall/activities/WallActivity$Companion;", "", "()V", "AFTER_HOT_WALL", "", "getAFTER_HOT_WALL", "()Ljava/lang/String;", "setAFTER_HOT_WALL", "(Ljava/lang/String;)V", "AFTER_NEW_WALL", "getAFTER_NEW_WALL", "setAFTER_NEW_WALL", "AFTER_TOP_WALL", "getAFTER_TOP_WALL", "setAFTER_TOP_WALL", "FAV_LIST", "FROM_FAV", "FROM_HIST", "GIF", "INDEX", "LIST", "NOTIFICATION_ID", "", "PRIMARY_CHANNEL_ID", "WALL_URL", "WRITE", "jsonToList", "Ljava/util/ArrayList;", "Lcom/mehul/redditwall/objects/BitURL;", "Lkotlin/collections/ArrayList;", "json", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getAFTER_NEW_WALL() {
            return null;
        }
        
        public final void setAFTER_NEW_WALL(@org.jetbrains.annotations.Nullable()
        java.lang.String p0) {
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getAFTER_HOT_WALL() {
            return null;
        }
        
        public final void setAFTER_HOT_WALL(@org.jetbrains.annotations.Nullable()
        java.lang.String p0) {
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getAFTER_TOP_WALL() {
            return null;
        }
        
        public final void setAFTER_TOP_WALL(@org.jetbrains.annotations.Nullable()
        java.lang.String p0) {
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Object jsonToList(@org.jetbrains.annotations.NotNull()
        java.lang.String json, @org.jetbrains.annotations.NotNull()
        kotlin.coroutines.Continuation<? super java.util.ArrayList<com.mehul.redditwall.objects.BitURL>> p1) {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}