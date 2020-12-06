package com.mehul.redditwall.activities;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u0000 \u001e2\u00020\u0001:\u0001\u001eB\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014J\u000e\u0010\u0015\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014J\b\u0010\u0016\u001a\u00020\u0012H\u0016J\u0012\u0010\u0017\u001a\u00020\u00122\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0015J\b\u0010\u001a\u001a\u00020\u0012H\u0016J\u000e\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014J\u0010\u0010\u001c\u001a\u00020\u00122\u0006\u0010\u001d\u001a\u00020\u0004H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001f"}, d2 = {"Lcom/mehul/redditwall/activities/SettingsActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "alarmChanged", "", "binding", "Lcom/mehul/redditwall/databinding/ActivitySettingsBinding;", "dark", "pending", "Landroid/app/PendingIntent;", "preferences", "Landroid/content/SharedPreferences;", "stateChanged", "wallAlarm", "Landroid/app/AlarmManager;", "wallChangeIntent", "Landroid/content/Intent;", "clearCache", "", "view", "Landroid/view/View;", "deleteDownloads", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onPause", "setRandomLocation", "showRandomSettings", "show", "Companion", "app_debug"})
public final class SettingsActivity extends androidx.appcompat.app.AppCompatActivity {
    private android.content.SharedPreferences preferences;
    private boolean dark = false;
    private boolean alarmChanged = false;
    private boolean stateChanged = false;
    private android.app.AlarmManager wallAlarm;
    private android.content.Intent wallChangeIntent;
    private android.app.PendingIntent pending;
    private com.mehul.redditwall.databinding.ActivitySettingsBinding binding;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PREVIEW_RES = "PREVIEWRES";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String SHOW_INFO = "INFOCARD";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String SORT_METHOD = "SORTIMG";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String IMG_WIDTH = "WIDTH";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String IMG_HEIGHT = "HEIGHT";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String DEFAULT = "DEFAULT";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String LOAD_SCALE = "LOAD";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String LOAD_GIF = "LOADGIF";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String DARK = "DARK";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String DOWNLOAD_ORIGIN = "DOWNLOAD_ORIGINAL";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String RANDOM_ENABLED = "SWITCHING_ENABLED";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String RANDOM_INTERVAL = "INTERVAL";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String RANDOM_LOCATION = "RAND_LOCATION";
    public static final int HOME = 0;
    public static final int LOCK = 1;
    public static final int BOTH = 2;
    public static final com.mehul.redditwall.activities.SettingsActivity.Companion Companion = null;
    private java.util.HashMap _$_findViewCache;
    
    @android.annotation.SuppressLint(value = {"SetTextI18n"})
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void showRandomSettings(boolean show) {
    }
    
    @java.lang.Override()
    public void onPause() {
    }
    
    @java.lang.Override()
    public void onBackPressed() {
    }
    
    public final void clearCache(@org.jetbrains.annotations.NotNull()
    android.view.View view) {
    }
    
    public final void setRandomLocation(@org.jetbrains.annotations.NotNull()
    android.view.View view) {
    }
    
    public final void deleteDownloads(@org.jetbrains.annotations.NotNull()
    android.view.View view) {
    }
    
    public SettingsActivity() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u000f\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/mehul/redditwall/activities/SettingsActivity$Companion;", "", "()V", "BOTH", "", "DARK", "", "DEFAULT", "DOWNLOAD_ORIGIN", "HOME", "IMG_HEIGHT", "IMG_WIDTH", "LOAD_GIF", "LOAD_SCALE", "LOCK", "PREVIEW_RES", "RANDOM_ENABLED", "RANDOM_INTERVAL", "RANDOM_LOCATION", "SHOW_INFO", "SORT_METHOD", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}