package com.mehul.redditwall.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.mehul.redditwall.R
import com.mehul.redditwall.databinding.ActivityPostBinding


class PostActivity : AppCompatActivity() {
    private var postLink: String? = ""
    private lateinit var binding: ActivityPostBinding
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        postLink = intent.getStringExtra(POST_LINK)
        supportActionBar?.title = intent.getStringExtra(POST_TITLE)
        val client = WebViewClient()

        binding.webPost.webViewClient = client
        binding.webPost.settings?.javaScriptEnabled = true
        binding.webPost.settings?.setAppCacheEnabled(true)
        binding.webPost.settings?.builtInZoomControls = true
        binding.webPost.settings?.saveFormData = true
        binding.webPost.loadUrl(postLink)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.web_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == R.id.refresh_web) {
            binding.webPost.loadUrl(postLink)
            return true
        } else if (id == android.R.id.home) {
            super.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.webPost.destroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return if (binding.webPost.canGoBack()) {
                binding.webPost.goBack()
                true
            } else {
                super.onBackPressed()
                true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    companion object {
        const val POST_LINK = "POSTLINK"
        const val POST_TITLE = "POSTTITLE"
    }
}
