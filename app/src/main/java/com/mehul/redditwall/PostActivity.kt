package com.mehul.redditwall

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity


class PostActivity : AppCompatActivity() {
    private var post: WebView? = null
    private var postLink: String? = ""

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        postLink = intent.getStringExtra(POST_LINK)
        post = findViewById(R.id.web_post)
        val client = WebViewClient()
        post?.webViewClient = client
        post?.settings?.javaScriptEnabled = true
        post?.settings?.setAppCacheEnabled(true)
        post?.settings?.builtInZoomControls = true
        post?.settings?.saveFormData = true
        post?.loadUrl(postLink)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.web_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.getItemId()
        if (id == R.id.refresh_web) {
            post?.loadUrl(postLink)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        post?.destroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (post?.canGoBack()!!) {
                post?.goBack()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    companion object {
        const val POST_LINK = "POSTLINK"
    }
}
