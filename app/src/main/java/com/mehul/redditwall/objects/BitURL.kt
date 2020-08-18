package com.mehul.redditwall.objects

import android.graphics.Bitmap
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class BitURL(var img: Bitmap?, @Expose @field:SerializedName("url")
internal val url: String, @Expose @field:SerializedName("post")
             internal val postLink: String,
             @Expose @field:SerializedName("preview_url")
             internal val previewUrl: String = "") {
    @Expose
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
}
