package com.nanda.notesapp.ui.splashscreen

import android.content.ClipData.newIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.nanda.notesapp.R
import com.nanda.notesapp.ui.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.handleCoroutineException

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}