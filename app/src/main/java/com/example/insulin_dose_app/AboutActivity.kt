package com.example.insulin_dose_app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.ComponentActivity

class AboutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        val change: ImageView = findViewById(R.id.imageView47)

        change.setOnClickListener {
            val intent = Intent(this@AboutActivity, RecordActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.imageView38).setOnClickListener {
            // Create an Intent to start the new activity
            val intent = Intent(this@AboutActivity, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}