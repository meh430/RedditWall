package com.mehul.redditwall;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

public class BitURL {
    @SerializedName("img")
    private Bitmap img;
    @SerializedName("url")
    private String url;
    @SerializedName("gif")
    private boolean containsGif = false;

    BitURL(Bitmap i, String u) {
        img = i;
        url = u;
        if (img == null) {
            containsGif = true;
        }
    }

    Bitmap getImg() {
        return img;
    }

    String getUrl() {
        return url;
    }

    boolean hasGif() {
        return containsGif;
    }

    void setGif(boolean gif) {
        containsGif = gif;
    }
}
