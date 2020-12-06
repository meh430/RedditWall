package com.mehul.redditwall.objects;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0006\u0018\u0000 \u00172\u00020\u0001:\u0001\u0017B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\b\u0010\u0011\u001a\u00020\u0012H\u0002J\u0006\u0010\u0013\u001a\u00020\u0012J\u0006\u0010\u0014\u001a\u00020\u0012J\u000e\u0010\u0015\u001a\u00020\u00122\u0006\u0010\u0016\u001a\u00020\u0005R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\t\u001a\u00020\n8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010\u00a8\u0006\u0018"}, d2 = {"Lcom/mehul/redditwall/objects/ProgressNotify;", "", "context", "Landroid/content/Context;", "size", "", "(Landroid/content/Context;I)V", "getContext", "()Landroid/content/Context;", "notificationBuilder", "Landroidx/core/app/NotificationCompat$Builder;", "getNotificationBuilder", "()Landroidx/core/app/NotificationCompat$Builder;", "notifyManager", "Landroid/app/NotificationManager;", "getSize", "()I", "createNotificationChannel", "", "finish", "sendNotification", "updateProgress", "progress", "Companion", "app_debug"})
public final class ProgressNotify {
    private android.app.NotificationManager notifyManager;
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    private final int size = 0;
    public static final int NOTIF_ID = 1;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String SECONDARY_CHANNEL_ID = "secondary_notification_channel";
    public static final com.mehul.redditwall.objects.ProgressNotify.Companion Companion = null;
    
    private final androidx.core.app.NotificationCompat.Builder getNotificationBuilder() {
        return null;
    }
    
    public final void sendNotification() {
    }
    
    public final void updateProgress(int progress) {
    }
    
    public final void finish() {
    }
    
    private final void createNotificationChannel() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final android.content.Context getContext() {
        return null;
    }
    
    public final int getSize() {
        return 0;
    }
    
    public ProgressNotify(@org.jetbrains.annotations.NotNull()
    android.content.Context context, int size) {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/mehul/redditwall/objects/ProgressNotify$Companion;", "", "()V", "NOTIF_ID", "", "SECONDARY_CHANNEL_ID", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}