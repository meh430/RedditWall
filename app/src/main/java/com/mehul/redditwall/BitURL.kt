package com.mehul.redditwall

import android.graphics.Bitmap

import com.google.gson.annotations.SerializedName

class BitURL(@field:SerializedName("img")
             internal val img: Bitmap?, @field:SerializedName("url")
             internal val url: String,
             @field:SerializedName("post")
             internal val postLink: String) {
    @SerializedName("gif")
    private var containsGif = false

    init {
        if (img == null) {
            containsGif = true
        }
    }

    fun hasGif(): Boolean {
        return containsGif
    }

    fun setGif(gif: Boolean) {
        containsGif = gif
    }

    fun getUrl(): String {
        return url
    }

    fun getImg(): Bitmap? {
        return img
    }
}
