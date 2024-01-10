package com.example.insulin_dose_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class pharmaceuticalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pharmaceutical)

        val goImageView: ImageView = findViewById(R.id.go)
        goImageView.setOnClickListener {
            startActivity(Intent(this, CerealsActivity::class.java))
        }

        val go1ImageView: ImageView = findViewById(R.id.go1)
        go1ImageView.setOnClickListener {
            startActivity(Intent(this, InsulinActivity::class.java))
        }

        findViewById<View>(R.id.imageView7).setOnClickListener {
            // Create an Intent to start the new activity
            val intent = Intent(this@pharmaceuticalActivity, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}
