package com.mehul.redditwall.favorites;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "fav_table")
public class FavImage {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "fav_url")
    @SerializedName("url")
    private String favUrl;
    @ColumnInfo(name = "fav_gif")
    @SerializedName("gif")
    private boolean isGif;

    public FavImage(int id, String favUrl, boolean isGif) {
        this.id = id;
        this.favUrl = favUrl;
        this.isGif = isGif;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFavUrl() {
        return favUrl;
    }

    public void setFavUrl(String favUrl) {
        this.favUrl = favUrl;
    }

    public boolean isGif() {
        return this.isGif;
    }

    public void setGif(boolean isGif) {
        this.isGif = isGif;
    }
}
