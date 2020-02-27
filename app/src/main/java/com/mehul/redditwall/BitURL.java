package com.mehul.redditwall;

import android.graphics.Bitmap;

class BitURL {
    private Bitmap img;
    private String url;

    BitURL(Bitmap i, String u) {
        img = i;
        url = u;
    }

    Bitmap getImg() {
        return img;
    }

    String getUrl() {
        return url;
    }
}
