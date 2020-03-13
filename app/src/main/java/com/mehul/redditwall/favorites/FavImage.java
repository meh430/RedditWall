package com.mehul.redditwall.favorites;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "fav_table")
public class FavImage {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "fav_url")
    private String favUrl;
    @ColumnInfo(name = "fav_gif")
    private boolean isGif;
    @ColumnInfo(name = "fav_post_link")
    private String postLink;

    public FavImage(int id, String favUrl, boolean isGif, String postLink) {
        this.id = id;
        this.favUrl = favUrl;
        this.isGif = isGif;
        this.postLink = postLink;
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

    public String getPostLink() {
        return postLink;
    }

    public void setPostLink(String postLink) {
        this.postLink = postLink;
    }
}
