package com.mehul.redditwall.objects;

import java.lang.System;

@androidx.room.Entity(tableName = "hist_table")
@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0019\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000  2\u00020\u0001:\u0001 B5\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0005\u0012\u0006\u0010\t\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\nJ\b\u0010\u001e\u001a\u00020\u001fH\u0002R\u001e\u0010\u0002\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001e\u0010\u000f\u001a\u00020\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R\u001e\u0010\t\u001a\u00020\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0011\"\u0004\b\u0015\u0010\u0013R\u001e\u0010\u0006\u001a\u00020\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0011\"\u0004\b\u0017\u0010\u0013R\u001e\u0010\u0007\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\f\"\u0004\b\u0019\u0010\u000eR\u001e\u0010\u0004\u001a\u00020\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u0011\"\u0004\b\u001b\u0010\u0013R\u001e\u0010\b\u001a\u00020\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u0011\"\u0004\b\u001d\u0010\u0013\u00a8\u0006!"}, d2 = {"Lcom/mehul/redditwall/objects/HistoryItem;", "", "id", "", "subName", "", "setDate", "source", "url", "postLink", "(ILjava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;)V", "getId", "()I", "setId", "(I)V", "internalDate", "getInternalDate", "()Ljava/lang/String;", "setInternalDate", "(Ljava/lang/String;)V", "getPostLink", "setPostLink", "getSetDate", "setSetDate", "getSource", "setSource", "getSubName", "setSubName", "getUrl", "setUrl", "convertDate", "", "Companion", "app_debug"})
public final class HistoryItem {
    @org.jetbrains.annotations.NotNull()
    @androidx.room.ColumnInfo(name = "hist_int_date")
    private java.lang.String internalDate;
    @androidx.room.PrimaryKey(autoGenerate = true)
    private int id;
    @org.jetbrains.annotations.NotNull()
    @androidx.room.ColumnInfo(name = "hist_name")
    private java.lang.String subName;
    @org.jetbrains.annotations.NotNull()
    @androidx.room.ColumnInfo(name = "hist_date")
    private java.lang.String setDate;
    @androidx.room.ColumnInfo(name = "hist_source")
    private int source;
    @org.jetbrains.annotations.NotNull()
    @androidx.room.ColumnInfo(name = "hist_url")
    private java.lang.String url;
    @org.jetbrains.annotations.NotNull()
    @androidx.room.ColumnInfo(name = "hist_post_link")
    private java.lang.String postLink;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String[] months = {"INIT", "Jan", "Feb", "March", "April", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};
    private static final java.lang.String[] ordinals = {"th", "st", "nd", "rd"};
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String[] sources = {"Downloaded", "Set on home screen", "Set on lock screen", "Set on both", "Set through refresh"};
    public static final int DOWNLOADED = 0;
    public static final int HOME_SCREEN = 1;
    public static final int LOCK_SCREEN = 2;
    public static final int BOTH = 3;
    public static final int REFRESH = 4;
    public static final com.mehul.redditwall.objects.HistoryItem.Companion Companion = null;
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getInternalDate() {
        return null;
    }
    
    public final void setInternalDate(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    private final void convertDate() {
    }
    
    public final int getId() {
        return 0;
    }
    
    public final void setId(int p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSubName() {
        return null;
    }
    
    public final void setSubName(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSetDate() {
        return null;
    }
    
    public final void setSetDate(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    public final int getSource() {
        return 0;
    }
    
    public final void setSource(int p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getUrl() {
        return null;
    }
    
    public final void setUrl(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPostLink() {
        return null;
    }
    
    public final void setPostLink(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    public HistoryItem(int id, @org.jetbrains.annotations.NotNull()
    java.lang.String subName, @org.jetbrains.annotations.NotNull()
    java.lang.String setDate, int source, @org.jetbrains.annotations.NotNull()
    java.lang.String url, @org.jetbrains.annotations.NotNull()
    java.lang.String postLink) {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\t\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0012\u001a\u00020\u000b2\u0006\u0010\u0013\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u0019\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n\u00a2\u0006\n\n\u0002\u0010\u000e\u001a\u0004\b\f\u0010\rR\u0016\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u000eR\u0019\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u000b0\n\u00a2\u0006\n\n\u0002\u0010\u000e\u001a\u0004\b\u0011\u0010\r\u00a8\u0006\u0014"}, d2 = {"Lcom/mehul/redditwall/objects/HistoryItem$Companion;", "", "()V", "BOTH", "", "DOWNLOADED", "HOME_SCREEN", "LOCK_SCREEN", "REFRESH", "months", "", "", "getMonths", "()[Ljava/lang/String;", "[Ljava/lang/String;", "ordinals", "sources", "getSources", "getOrdinal", "n", "app_debug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String[] getMonths() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String[] getSources() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getOrdinal(int n) {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}