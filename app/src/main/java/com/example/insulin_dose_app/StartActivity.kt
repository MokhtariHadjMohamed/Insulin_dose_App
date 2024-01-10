package com.example.insulin_dose_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.Toast
import androidx.core.view.ViewCompat

class StartActivity : AppCompatActivity(), OnClickListener {

    lateinit var logIn: Button
    lateinit var register:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        logIn = findViewById(R.id.logInStartActivity)
        logIn.setOnClickListener(this)

        register = findViewById(R.id.registerBtnStartActivity)
        register.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            logIn -> {
                startActivity(Intent(this, LogInActivity::class.java))
            }
            register -> {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
        }
    }
}