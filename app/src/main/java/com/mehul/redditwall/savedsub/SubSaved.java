package com.mehul.redditwall.savedsub;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sub_table")
public class SubSaved {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "sub_name")
    private String subName;
    @ColumnInfo(name = "sub_date")
    private String subDate;

    public SubSaved(int id, String subName, String subDate) {
        this.id = id;
        this.subName = subName;
        this.subDate = subDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getSubDate() {
        return subDate;
    }

    public void setSubDate(String subDate) {
        this.subDate = subDate;
    }
}
