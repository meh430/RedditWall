package com.mehul.redditwall.objects;

import java.lang.System;

@androidx.room.Entity(tableName = "sub_table")
@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0019\n\u0002\u0010\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B7\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0005\u0012\b\u0010\t\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\nJ\b\u0010\u001e\u001a\u00020\u001fH\u0002R\u001e\u0010\u0002\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR \u0010\u000f\u001a\u0004\u0018\u00010\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R \u0010\t\u001a\u0004\u0018\u00010\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0011\"\u0004\b\u0015\u0010\u0013R\u001e\u0010\u0006\u001a\u00020\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0011\"\u0004\b\u0017\u0010\u0013R\u001e\u0010\b\u001a\u00020\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0011\"\u0004\b\u0019\u0010\u0013R\u001e\u0010\u0004\u001a\u00020\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u0011\"\u0004\b\u001b\u0010\u0013R\u001e\u0010\u0007\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\f\"\u0004\b\u001d\u0010\u000e\u00a8\u0006 "}, d2 = {"Lcom/mehul/redditwall/objects/Subreddit;", "", "id", "", "subName", "", "subDesc", "subscribers", "subIcon", "subDate", "(ILjava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;)V", "getId", "()I", "setId", "(I)V", "internalDate", "getInternalDate", "()Ljava/lang/String;", "setInternalDate", "(Ljava/lang/String;)V", "getSubDate", "setSubDate", "getSubDesc", "setSubDesc", "getSubIcon", "setSubIcon", "getSubName", "setSubName", "getSubscribers", "setSubscribers", "convertDate", "", "app_debug"})
public final class Subreddit {
    @org.jetbrains.annotations.Nullable()
    @androidx.room.ColumnInfo(name = "sub_int_date")
    private java.lang.String internalDate;
    @androidx.room.PrimaryKey(autoGenerate = true)
    private int id;
    @org.jetbrains.annotations.NotNull()
    @androidx.room.ColumnInfo(name = "sub_name")
    private java.lang.String subName;
    @org.jetbrains.annotations.NotNull()
    @androidx.room.ColumnInfo(name = "sub_desc")
    private java.lang.String subDesc;
    @androidx.room.ColumnInfo(name = "sub_subs")
    private int subscribers;
    @org.jetbrains.annotations.NotNull()
    @androidx.room.ColumnInfo(name = "sub_icon")
    private java.lang.String subIcon;
    @org.jetbrains.annotations.Nullable()
    @androidx.room.ColumnInfo(name = "sub_date")
    private java.lang.String subDate;
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getInternalDate() {
        return null;
    }
    
    public final void setInternalDate(@org.jetbrains.annotations.Nullable()
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
    public final java.lang.String getSubDesc() {
        return null;
    }
    
    public final void setSubDesc(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    public final int getSubscribers() {
        return 0;
    }
    
    public final void setSubscribers(int p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSubIcon() {
        return null;
    }
    
    public final void setSubIcon(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSubDate() {
        return null;
    }
    
    public final void setSubDate(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    public Subreddit(int id, @org.jetbrains.annotations.NotNull()
    java.lang.String subName, @org.jetbrains.annotations.NotNull()
    java.lang.String subDesc, int subscribers, @org.jetbrains.annotations.NotNull()
    java.lang.String subIcon, @org.jetbrains.annotations.Nullable()
    java.lang.String subDate) {
        super();
    }
}