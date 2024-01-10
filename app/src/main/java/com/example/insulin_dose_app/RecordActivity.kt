package com.example.insulin_dose_app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.ComponentActivity

class RecordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.record)
        val change: ImageView = findViewById(R.id.imageView27)

        change.setOnClickListener {
            val intent = Intent(this@RecordActivity, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}