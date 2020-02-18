package com.mehul.redditwall;

import android.graphics.Bitmap;

public class BitURL {
    private Bitmap img;
    private String url;

    public BitURL(Bitmap i, String u) {
        img = i;
        url = u;
    }

    public Bitmap getImg() {
        return img;
    }

    public String getUrl() {
        return url;
    }
}
