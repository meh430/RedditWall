package com.mehul.redditwall.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mehul.redditwall.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    //TODO: actually do some work here
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = ""
        val main = Intent(this, MainActivity::class.java).apply {
            putExtra(MAIN, true)
        }

        startActivity(main)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish()
    }

    companion object {
        const val MAIN = "launchMain"
    }
}
