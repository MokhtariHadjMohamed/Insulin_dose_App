package com.example.insulin_dose_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class AccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)


        findViewById<View>(R.id.imageView39).setOnClickListener {
            // Create an Intent to start the new activity
            val intent = Intent(this@AccountActivity, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}